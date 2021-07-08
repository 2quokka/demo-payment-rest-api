package com.payment.api.payment.service;

import com.payment.api.card.CardInfo;
import com.payment.api.common.GenDataForForwarding;
import com.payment.api.exception.*;
import com.payment.api.payment.dto.*;
import com.payment.api.payment.entity.CardCompanyData;
import com.payment.api.payment.entity.PaymentInfo;
import com.payment.api.payment.repository.CardCompanyDataRepository;
import com.payment.api.payment.repository.PaymentInfoRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service @AllArgsConstructor
public class PaymentService {

    private final ModelMapper modelMapper;

    private final PaymentInfoRepository paymentInfoRepository;

    private final CardCompanyDataRepository cardCompanyDataRepository;

    /**
     * 1. 결제
     * @param paymentInfoDTO
     * @return PaymentResponse
     */
    @Transactional
    public PaymentResponse payment(PaymentInfoDTO paymentInfoDTO){
        PaymentResponse rs = new PaymentResponse();

        /*
         *  부가가치세 계산하기
         */
        if(paymentInfoDTO.getVat() == null) {
            paymentInfoDTO.setVat(Math.round((float)paymentInfoDTO.getAmount()/11));
        }

        //컨버터 적용
        //맵핑
        if (modelMapper.getTypeMap(PaymentInfoDTO.class, PaymentInfo.class) == null) {
            modelMapper.addMappings(new PaymentInfoMapper());
        }
        PaymentInfo paymentInfo = modelMapper.map(paymentInfoDTO, PaymentInfo.class);

        //카드정보 암호화하여 셋팅
        paymentInfo.setEncCardInfo(new CardInfo(paymentInfoDTO.getCardNum(), paymentInfoDTO.getExpiryDate(), paymentInfoDTO.getCvcNum())
                .encCardInfo());

        /*
         * 나머지 값 셋팅
         */
        paymentInfo.setState(State.PAYMENT); //결제상태코드
        paymentInfo.setApprovalTime(LocalDateTime.now());

        //INSERT
        PaymentInfo savePaymentInfo = this.paymentInfoRepository.save(paymentInfo);

        //카드사 전송
        CardCompanyData saveCardCompanyData = cardCompanyDataRepository.save(genCardCompanyData(savePaymentInfo));
        rs.setPaymentId(savePaymentInfo.getPaymentId());
        rs.setData(saveCardCompanyData.getData());
        return rs;
    }

    /**
     * 2. 결제 전체취소, (부분취소)
     * @param cancelPaymentDTO
     * @return PaymentResponse
     */
    @Transactional
    public PaymentResponse cancelPay(CancelPaymentDTO cancelPaymentDTO){
        PaymentResponse rs = new PaymentResponse();

        String id = cancelPaymentDTO.getPaymentId();
            //관리번호로 결제데이터 조회, 없을경우 Not found 에러
            PaymentInfo paymentInfo = paymentInfoRepository.findbyIdForUpdate(id)
                    .orElseThrow(() -> new PaymentNotFoundException(id));

            // 취소건 인경우
            if (paymentInfo.getState() == State.CANCEL) {
                throw new NotPaymentIdException(id);
            }
            //이미 전체 취소된 경우 (Payment의 finalAmount, finalVat 0 일경우)
            if (paymentInfo.getFinalAmount() == 0 && paymentInfo.getFinalVat() == 0) {
                throw new AlreadyCancelException(id);
            }

            Integer amount = paymentInfo.getFinalAmount();
            Integer vat = paymentInfo.getFinalVat();
            Integer cancelAmount = cancelPaymentDTO.getCancelAmount();
            Integer cancelVat = cancelPaymentDTO.getCancelVat();

            //부가가치세가 안들어왔을 경우 계산
            if (cancelVat == null) {
                cancelPaymentDTO.setCancelVat(Math.round((float) cancelPaymentDTO.getCancelAmount() / 11));
                cancelVat = cancelPaymentDTO.getCancelVat();
            }
            //취소 부가가치세가 남은 부가가치세보다 클 경우
            else if (cancelVat > vat) {
                throw new ExcessVatException(vat);
            }

            //취소금액이 결제 금액보다 클 경우
            if (cancelAmount > amount) {
                throw new ExcessAmountException(amount);
            }

            Integer restAmount = amount - cancelAmount;
            Integer restVat = vat - cancelVat;

            if (restAmount == 0 || restVat < 0) {
                cancelVat = vat;
                restVat = 0;
            }

            if (restVat > restAmount) {
                throw new ExcessRestVatException(restVat);
            }

            PaymentInfo cancelPaymentInfo = PaymentInfo.builder()
                    .encCardInfo(paymentInfo.getEncCardInfo())
                    .installments("00")     // 취소시, 일시불
                    .amount(cancelAmount)
                    .vat(cancelVat)
                    .finalAmount(restAmount)
                    .finalVat(restVat)
                    .state(State.CANCEL)
                    .approvalTime(paymentInfo.getApprovalTime())
                    .cancleTime(LocalDateTime.now())
                    .orginPaymentId(paymentInfo.getPaymentId())
                    .build();
            //취소결제정보저장
            PaymentInfo savePaymentInfo = this.paymentInfoRepository.save(cancelPaymentInfo);

            //결제 남은금액 업데이트
            paymentInfo.setFinalAmount(restAmount);
            paymentInfo.setFinalVat(restVat);
//            paymentInfoRepository.save(paymentInfo);

            //카드사 전송
            CardCompanyData saveCardCompanyData = cardCompanyDataRepository.save(genCardCompanyData(savePaymentInfo));

            rs.setPaymentId(savePaymentInfo.getPaymentId());
            rs.setData(saveCardCompanyData.getData());
        return rs;
    }

    public SearchResponse searchPayment(PaymentIdDTO paymentIdDTO) {
        String id = paymentIdDTO.getPaymentId();

        //관리번호로 결제데이터 조회, 없을경우 Not found 에러
        PaymentInfo paymentInfo = paymentInfoRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));

        //카드정보 복호화
        CardInfo cardInfo = CardInfo.getDecCardInfo(paymentInfo.getEncCardInfo());

        String cardNum = cardInfo.getCardNum();

        //마스킹
        if (cardNum != null && !"".equals(cardNum)) {
            // 가운데 글자 마스킹 변수 선언
            String middleMask = cardNum.substring(6, cardNum.length()-3);
            // 마스킹 변수 선언(*)
            String masking = "";
            // 앞 6자리, 맨뒤 3자리를 빼고 모두 마스킹 하기위한 증감값
            for (int i = 0; i < middleMask.length(); i++){ masking += "*"; }
            cardNum = cardNum.substring(0,6)
                    + masking
                    + cardNum.substring(cardNum.length()-3, cardNum.length());
        }
        cardInfo.setCardNum(cardNum);

        SearchResponse searchResponse = SearchResponse.builder()
                .paymentId(paymentInfo.getPaymentId())
                .cardInfo(cardInfo)
                .state(paymentInfo.getState())
                .amount(paymentInfo.getAmount())
                .vat(paymentInfo.getVat())
                .build();

        if( paymentInfo.getState() == State.PAYMENT) {
            searchResponse.setFinalAmount(paymentInfo.getFinalAmount());
            searchResponse.setFinalVat(paymentInfo.getFinalVat());
        }
        return searchResponse;
    }

    /*
    *  1. 카드사 전달용 데이터 변환
    * */
    public CardCompanyData genCardCompanyData(PaymentInfo paymentInfo) {
        CardCompanyData cardCompanyData = new CardCompanyData();

        System.out.println(paymentInfo.toString());
        //결제정보->카드사전달데이터 중복 맵핑 추가 방지.
        if (modelMapper.getTypeMap(PaymentInfo.class, GenDataForForwarding.class) == null) {
            modelMapper.addMappings(new PaymentDataMapper());
        }
        GenDataForForwarding genDataForForwarding = modelMapper.map(paymentInfo, GenDataForForwarding.class);
        if (paymentInfo.getState() != null) {
            genDataForForwarding.setPaymentType(paymentInfo.getState().toString());
        }
        //카드사 전달용 데이터 생성
        String forwardData = genDataForForwarding.generateData();
        cardCompanyData.setData(forwardData);

        return cardCompanyData;
    }

}
