package com.payment.api.payment.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/*
 * 전달 받고자 하는 데이터만 선언한 클래스
 * 요청받을때 사용.
 * */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CancelPaymentDTO {

    @NotNull
    private String paymentId;   //관리번호

    @NotNull
    @Min(100) @Max(1000000000)
    private Integer cancelAmount;     //취소금액

    @Min(0) @Max(1000000000)
    private Integer cancelVat;  // 취소 부가가치세

}
