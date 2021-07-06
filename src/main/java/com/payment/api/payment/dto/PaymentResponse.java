package com.payment.api.payment.dto;

import lombok.*;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter

/*
    결제요청 응답 클래스
*/
public class PaymentResponse {
    private String paymentId;
    private String data;
}
