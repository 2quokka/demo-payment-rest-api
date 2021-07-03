package com.payment.demopaymentrestapi.cardPay;

import com.payment.demopaymentrestapi.mapper.PaymentInfoMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    private final ModelMapper modelMapper;
    private final PaymentInfoRepository paymentInfoRepository;

    /*
    * 1. 결제 API
    * 카드정보, 금액정보를 입력받아 암호화 및 레이아웃에 맞게 변환하여 카드사에 전달한다.
    * */
    @PostMapping(value = "/payment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity payment(@RequestBody PaymentInfoDTO paymentInfoDTO){
        //컨버터 적용
        modelMapper.addMappings(new PaymentInfoMapper());

        PaymentInfo paymentInfo = modelMapper.map(paymentInfoDTO, PaymentInfo.class);

        //paymentId(관리번호) 20자리 채번
        paymentInfo.setPaymentId("1");

        //나머지 값 셋팅
        paymentInfo.setFinalAmount(paymentInfo.getAmount());
        paymentInfo.setFinalVat(paymentInfo.getVat());
        paymentInfo.setStatus("00"); //결제상태코드 00:승
        paymentInfo.setApprovalTime(LocalDateTime.now());

        //INSERT
        PaymentInfo savePaymentInfo = this.paymentInfoRepository.save(paymentInfo);

        //카드사 전송
        //String 변환 후 DB 저장

        URI createURI = linkTo(methodOn(PaymentController.class).payment(null)).slash(paymentInfo.getPaymentId()).toUri();

        return ResponseEntity.created(createURI).body(paymentInfo);
    }

}
