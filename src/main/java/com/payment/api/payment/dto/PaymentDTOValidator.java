package com.payment.api.payment.dto;

import com.payment.api.payment.dto.PaymentInfoDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class PaymentDTOValidator {

    public void validate(PaymentInfoDTO paymentInfoDTO, Errors errors) {

        //부가가치세가 결제금액보다 클 수 없음
        if (paymentInfoDTO.getVat() != null) {
            if (paymentInfoDTO.getVat() > paymentInfoDTO.getAmount()) {
                errors.rejectValue("vat", "부가가치세는 결제금액보다 클 수 없습니다.");
            }
        }
    }
}
