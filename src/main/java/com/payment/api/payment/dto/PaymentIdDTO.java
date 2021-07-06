package com.payment.api.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
/*
 * 전달 받고자 하는 데이터만 선언한 클래스
 * 요청받을때 사용.
 * 결제데이터 찾을 때 Request
 * */

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PaymentIdDTO<paymentId> {
    @NotNull
    private String paymentId;
}
