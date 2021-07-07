package com.payment.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse> paymentNotfound(PaymentNotFoundException ex) {
        ErrorResponse response =
                new ErrorResponse("error-0000", "해당 결제 건이 존재하지 않습니다." + ex.getPaymentId());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotPaymentIdException.class)
    public ResponseEntity<ErrorResponse> notPaymentId(NotPaymentIdException ex) {
        ErrorResponse response =
                new ErrorResponse("error-0001", "취소 건 입니다." + ex.getPaymentId());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyCancelException.class)
    public ResponseEntity<ErrorResponse> alreadyCancel(AlreadyCancelException ex) {
        ErrorResponse response =
                new ErrorResponse("error-0002", "이미 취소된 건 입니다." + ex.getPaymentId());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExcessAmountException.class)
    public ResponseEntity<ErrorResponse> excessAmount(ExcessAmountException ex) {
        ErrorResponse response =
                new ErrorResponse("error-0003", "취소금액이 결제금액을 초과합니다." + ex.getAmount());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExcessVatException.class)
    public ResponseEntity<ErrorResponse> excessVat(ExcessVatException ex) {
        ErrorResponse response =
                new ErrorResponse("error-0004", "취소 부가가치세가 결제 부가가치세를 초과합니다." + ex.getVat());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExcessRestVatException.class)
    public ResponseEntity<ErrorResponse> excessRestVat(ExcessRestVatException ex) {
        ErrorResponse response =
                new ErrorResponse("error-0005", "남은 부가가치세가 남은 금액보다 큽니다." + ex.getVat());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
