package com.payment.api.payment.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExcessRestVatException extends RuntimeException{
    private Integer vat;
}
