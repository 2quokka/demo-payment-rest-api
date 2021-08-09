package com.payment.api.payment.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExcessVatException extends RuntimeException{
    private Integer vat;
}
