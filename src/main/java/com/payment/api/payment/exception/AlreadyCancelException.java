package com.payment.api.payment.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class AlreadyCancelException extends RuntimeException{
    private String paymentId;
}
