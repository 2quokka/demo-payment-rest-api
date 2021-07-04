package com.payment.demopaymentrestapi.payment;

import com.payment.demopaymentrestapi.card_company.CardCompany;
import com.payment.demopaymentrestapi.card_company.CardCompanyRepository;
import com.payment.demopaymentrestapi.common.DataOfCardCompany;
import com.payment.demopaymentrestapi.common.PaymentId;
import com.payment.demopaymentrestapi.mapper.PaymentDataMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class PaymentController {

    private final ModelMapper modelMapper;

    private final PaymentInfoRepository paymentInfoRepository;

    private final PaymentDTOValidator paymentDTOValidator;

    private final CardCompanyRepository cardCompanyRepository;
    /*
    * 1. 결제 API
    * 카드정보, 금액정보를 입력받아 암호화 및 레이아웃에 맞게 변환하여 카드사에 전달한다.
    * */
    @PostMapping(value = "/payment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity payment(@RequestBody @Valid PaymentInfoDTO paymentInfoDTO, Errors errors){
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        //추가 Validation
        paymentDTOValidator.validate(paymentInfoDTO, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        //컨버터 적용
        //modelMapper.addMappings(new PaymentInfoMapper());
        //맵핑
        PaymentInfo paymentInfo = modelMapper.map(paymentInfoDTO, PaymentInfo.class);

        //paymentId(관리번호) 20자리 채번
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("simple-jpa-application");
        EntityManager em = emf.createEntityManager();

        PaymentId paymentId = new PaymentId();
        em.persist(paymentId);

        String padPaymentId = String.format("%20d", paymentId.getId());

        paymentInfo.setPaymentId(padPaymentId);

        //나머지 값 셋팅
        paymentInfo.setFinalAmount(paymentInfo.getAmount());
        paymentInfo.setFinalVat(paymentInfo.getVat());
        paymentInfo.setStatus("00"); //결제상태코드 00:승인
        paymentInfo.setApprovalTime(LocalDateTime.now());

        //INSERT
        PaymentInfo savePaymentInfo = this.paymentInfoRepository.save(paymentInfo);

        /*
        * 카드사 전송
        */
        CardCompany cardCompany = new CardCompany();

        //결제정보->카드사전달데이터 맵핑
        modelMapper.addMappings(new PaymentDataMapper());
        DataOfCardCompany dataOfCardCompany = modelMapper.map(savePaymentInfo, DataOfCardCompany.class);

        //카드사 전달용 데이터 생성
        cardCompany.setData(dataOfCardCompany.generateData());
        CardCompany saveCardCompany = this.cardCompanyRepository.save(cardCompany);

        return ResponseEntity.status(HttpStatus.CREATED).body(paymentInfo);
    }

}
