package com.payment.api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotPaymentIdException extends RuntimeException{
    private String paymentId;
}
