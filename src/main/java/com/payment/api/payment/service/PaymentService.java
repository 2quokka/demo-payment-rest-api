package com.payment.api.payment.service;

import com.payment.api.card_company.CardCompany;
import com.payment.api.card_company.CardCompanyRepository;
import com.payment.api.common.DataOfCardCompany;
import com.payment.api.common.SecCardInfo;
import com.payment.api.payment.dto.PaymentDataMapper;
import com.payment.api.payment.dto.State;
import com.payment.api.payment.entity.PaymentId;
import com.payment.api.payment.entity.PaymentInfo;
import com.payment.api.payment.repository.PaymentInfoRepository;
import com.payment.api.payment.dto.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

@Service @AllArgsConstructor
public class PaymentService {

    private final ModelMapper modelMapper;

    private final PaymentInfoRepository paymentInfoRepository;

    private final CardCompanyRepository cardCompanyRepository;

    public PaymentResponse payment(PaymentInfoDTO paymentInfoDTO){
        PaymentResponse rs = new PaymentResponse();
        /*
        *  부가가치세 계산하기
        */
        int vat = 0;
        if(paymentInfoDTO.getVat() == null) {
            vat = Math.round((float)paymentInfoDTO.getAmount()/11);
        }
        paymentInfoDTO.setVat(vat);
        System.out.println("calculated vat = " + vat);

        //컨버터 적용
        //modelMapper.addMappings(new PaymentInfoMapper());
        //맵핑
        PaymentInfo paymentInfo = modelMapper.map(paymentInfoDTO, PaymentInfo.class);

        //paymentId(관리번호) 20자리 채번
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("simple-jpa-application");
        EntityManager em = emf.createEntityManager();

        PaymentId paymentId = new PaymentId();
        em.persist(paymentId);

        String padPaymentId = String.format("%020d", paymentId.getId());

        paymentInfo.setPaymentId(padPaymentId);

        /*
         * 나머지 값 셋팅
         */
        paymentInfo.setFinalAmount(paymentInfo.getAmount());
        paymentInfo.setFinalVat(paymentInfo.getVat());
        paymentInfo.setStatus(State.OK); //결제상태코드 00:승인
        paymentInfo.setApprovalTime(LocalDateTime.now());

        //INSERT
        PaymentInfo savePaymentInfo = this.paymentInfoRepository.save(paymentInfo);

        /*
         * 카드사 전송
         */
        CardCompany cardCompany = new CardCompany();

        //결제정보->카드사전달데이터 중복 맵핑 추가 방지.
        TypeMap tm = modelMapper.getTypeMap(PaymentInfo.class, DataOfCardCompany.class);
        if (tm == null) {
            modelMapper.addMappings(new PaymentDataMapper());
        }
        DataOfCardCompany dataOfCardCompany = modelMapper.map(savePaymentInfo, DataOfCardCompany.class);

        //카드정보 암호화
        SecCardInfo secCardInfo = new SecCardInfo(
                savePaymentInfo.getCardNum()
                , savePaymentInfo.getExpiryDate()
                , savePaymentInfo.getCvcNum()
        );
        dataOfCardCompany.setEncCardInfo(secCardInfo.encCardInfo());

        //카드사 전달용 데이터 생성
        String forwardData = dataOfCardCompany.generateData();
        cardCompany.setData(forwardData);
        CardCompany saveCardCompany = this.cardCompanyRepository.save(cardCompany);

        rs.setPaymentId(savePaymentInfo.getPaymentId());
        rs.setData(forwardData);
        return rs;
    }

    public PaymentResponse cancelPay(CancelPaymentDTO cancelPaymentDTO){
        PaymentResponse rs = new PaymentResponse();

        //관리번호로 결제데이터 조회

        //결제금액으로 부분취소인지 확인

        //부가가치세 계산

        //전체취소일 경우

        //부분취소일 경우

        return rs;
    }

}
