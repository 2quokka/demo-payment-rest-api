package com.payment.api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExcessVatException extends RuntimeException{
    private Integer vat;
}
