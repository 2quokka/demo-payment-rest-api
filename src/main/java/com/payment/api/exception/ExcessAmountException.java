package com.payment.api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExcessAmountException extends RuntimeException{
    private Integer amount;
}
