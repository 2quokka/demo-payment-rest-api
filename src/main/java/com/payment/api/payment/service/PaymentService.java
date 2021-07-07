package com.payment.api.payment.service;

import com.payment.api.exception.AlreadyCancelException;
import com.payment.api.exception.ExcessAmountException;
import com.payment.api.exception.ExcessVatException;
import com.payment.api.payment.entity.CardCompanyData;
import com.payment.api.payment.repository.CancelPaymentRepository;
import com.payment.api.payment.repository.CardCompanyDataRepository;
import com.payment.api.common.GenDataForForwarding;
import com.payment.api.common.SecCardInfo;
import com.payment.api.exception.PaymentNotFoundException;
import com.payment.api.payment.dto.*;
import com.payment.api.payment.entity.CancelPayment;
import com.payment.api.payment.entity.PaymentInfo;
import com.payment.api.payment.repository.PaymentInfoRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service @AllArgsConstructor
public class PaymentService {

    private final ModelMapper modelMapper;

    private final PaymentInfoRepository paymentInfoRepository;

    private final CardCompanyDataRepository cardCompanyDataRepository;

    private final CancelPaymentRepository cancelPaymentRepository;

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

        /*
         * 나머지 값 셋팅
         */
        paymentInfo.setFinalAmount(paymentInfo.getAmount());
        paymentInfo.setFinalVat(paymentInfo.getVat());
        paymentInfo.setStatus(State.OK); //결제상태코드 00:승인
        paymentInfo.setApprovalTime(LocalDateTime.now());

        //INSERT
        PaymentInfo savePaymentInfo = this.paymentInfoRepository.save(paymentInfo);

        //카드사 전송, 암호화
        CardCompanyData saveCardCompanyData = sendCardCompanyData(savePaymentInfo);

        rs.setPaymentId(savePaymentInfo.getPaymentId());
        rs.setData(saveCardCompanyData.getData());
        return rs;
    }

    public PaymentResponse cancelPay(CancelPaymentDTO cancelPaymentDTO){
        PaymentResponse rs = new PaymentResponse();
        CancelPayment cancelPayment = new CancelPayment();

        String id = cancelPaymentDTO.getPaymentId();


        //관리번호로 결제데이터 조회, 없을경우 Not found 에러
        PaymentInfo paymentInfo = paymentInfoRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));

        //이미 전체 취소된 경우
        if (paymentInfo.getStatus() == State.ALL_CANCEL) {
            throw new AlreadyCancelException(id);
        }

        cancelPayment = modelMapper.map(cancelPaymentDTO, CancelPayment.class);

        Integer finalAmount = paymentInfo.getFinalAmount();
        Integer finalVat = paymentInfo.getFinalVat();
        Integer cancelAmount = cancelPayment.getCancelAmount();
        Integer cancelVat = cancelPayment.getCancelVat();

        //취소금액이 결제 남은금액보다 클 경우
        if (cancelAmount > finalAmount) {
            throw new ExcessAmountException(finalAmount);
        }

        //부가가치세가 안들어왔을 경우 계산
        if (cancelVat == null) {
            cancelPayment.setCancelVat(Math.round((float)cancelAmount/11));
            cancelVat = cancelPayment.getCancelVat();
        }
        //취소 부가가치세가 남은 부가가치세보다 클 경우
        else if (cancelVat > finalVat) {
            throw new ExcessVatException(finalVat);
        }

        Integer restAmount = finalAmount - cancelAmount;
        Integer restVat = finalVat - cancelVat;

        //남은 금액이 0일 경우, 전체취소
        if (restAmount == 0 && restVat == 0) {
            paymentInfo.setStatus(State.ALL_CANCEL); //전체취소 상태 변경
            paymentInfo.setInstallments("00"); //일시불
        }
        //부분취소
        else {
            paymentInfo.setStatus(State.PART_CANCEL); // 부분취소 상태 변경
        }
        paymentInfo.setFinalAmount(restAmount); // 남은금액
        paymentInfo.setFinalVat(restVat); //남은 부가가치세

        cancelPayment.setCancelTime(LocalDateTime.now());
        cancelPayment.setSeqNum(cancelPaymentRepository.countbyPaymentId(id)+1);

        //취소내역 INSERT
        CancelPayment saveCancelPayment = cancelPaymentRepository.save(cancelPayment);

        //결제정보 UPDATE
        PaymentInfo savePaymentInfo = this.paymentInfoRepository.save(paymentInfo);

        //카드사 전송, 암호화
        CardCompanyData saveCardCompanyData = sendCardCompanyData(savePaymentInfo);

        rs.setPaymentId(savePaymentInfo.getPaymentId());
        rs.setData(saveCardCompanyData.getData());

        return rs;
    }

    /*
    *  1. 카드사 전달용 데이터 변환
    *  2. 데이터 암호화
    *  3. 데이터 저장
    * */
    public CardCompanyData sendCardCompanyData(PaymentInfo paymentInfo) {
        CardCompanyData cardCompanyData = new CardCompanyData();

        //결제정보->카드사전달데이터 중복 맵핑 추가 방지.
        if (modelMapper.getTypeMap(PaymentInfo.class, GenDataForForwarding.class) == null) {
            modelMapper.addMappings(new PaymentDataMapper());
        }
        GenDataForForwarding genDataForForwarding = modelMapper.map(paymentInfo, GenDataForForwarding.class);

        if ( paymentInfo.getStatus() == State.OK) {
            genDataForForwarding.setPaymentType("PAYMENT");
        }
        else {
            genDataForForwarding.setPaymentType("CANCEL");
        }

        //카드정보 암호화
        SecCardInfo secCardInfo = new SecCardInfo(
                paymentInfo.getCardNum()
                , paymentInfo.getExpiryDate()
                , paymentInfo.getCvcNum()
        );
        genDataForForwarding.setEncCardInfo(secCardInfo.encCardInfo());

        //카드사 전달용 데이터 생성
        String forwardData = genDataForForwarding.generateData();
        cardCompanyData.setData(forwardData);

        return this.cardCompanyDataRepository.save(cardCompanyData);
    }

}
