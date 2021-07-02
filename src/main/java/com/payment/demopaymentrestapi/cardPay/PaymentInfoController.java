package com.payment.demopaymentrestapi.cardPay;

import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
public class PaymentInfoController {

    private final ModelMapper modelMapper;

    private final PaymentInfoRepository paymentInfoRepository;

    public PaymentInfoController(ModelMapper modelMapper, PaymentInfoRepository paymentInfoRepository) {
        this.modelMapper = modelMapper;
        this.paymentInfoRepository = paymentInfoRepository;
    }

    /*
    * 1. 결제 API
    * 카드정보, 금액정보를 입력받아 암호화 및 레이아웃에 맞게 변환하여 카드사DB에 저장한다.
    * */
    @PostMapping(value = "/api/payment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity payment(@RequestBody PaymentInfoDTO paymentInfoDTO){
        PaymentInfo paymentInfo = modelMapper.map(paymentInfoDTO, PaymentInfo.class);
        PaymentInfo savePaymentInfo = this.paymentInfoRepository.save(paymentInfo);
        URI createURI = linkTo(methodOn(PaymentInfoController.class).payment(null)).slash(paymentInfo.getPaymentId()).toUri();
        return ResponseEntity.created(createURI).body(paymentInfo);
    }

}
