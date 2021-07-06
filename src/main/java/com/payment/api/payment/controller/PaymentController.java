package com.payment.api.payment.controller;

import com.payment.api.payment.repository.PaymentDTOValidator;
import com.payment.api.payment.dto.PaymentResponse;
import com.payment.api.payment.service.PaymentService;
import com.payment.api.payment.dto.CancelPaymentDTO;
import com.payment.api.payment.dto.PaymentIdDTO;
import com.payment.api.payment.dto.PaymentInfoDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class PaymentController {

    private final PaymentDTOValidator paymentDTOValidator;
    private final PaymentService paymentService;
    /*
    * 1. 결제 API
    * 카드정보, 금액정보를 입력받아 암호화 및 레이아웃에 맞게 변환하여 카드사에 전달한다.
    */
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

        PaymentResponse rs = paymentService.payment(paymentInfoDTO);

        //성공시, 관리번호, 카드사 전달한 데이터
        return ResponseEntity.status(HttpStatus.CREATED).body(rs);
    }

    /*
     * 2. 결제 취소 API  (부분취소)
     */
    @PostMapping(value = "/cancelPay", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity cancelPay(@RequestBody @Valid CancelPaymentDTO cancelPaymentDTO, Errors errors){
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        //성공시, 관리번호, 카드사 전달한 데이터
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /*
     * 3. 결제 데이터 조회
     */
    @GetMapping(value = "/payment-info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getPayInfo(@RequestParam @Valid PaymentIdDTO paymentIdDTO, Errors errors){
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        //성공시, 관리번호, 카드사 전달한 데이터
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
