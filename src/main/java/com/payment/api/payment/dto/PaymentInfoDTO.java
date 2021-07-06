package com.payment.api.payment.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/*
* 전달 받고자 하는 데이터만 선언한 클래스
* 요청받을때 사용.
* */
@Data @Builder @NoArgsConstructor @AllArgsConstructor @ToString(includeFieldNames = true)
public class PaymentInfoDTO {

    @NotNull
    @Size(min = 10, max = 16)
    private String cardNum; //카드번호(10 ~ 16자리 숫자)

    @NotNull
    @Size(min = 4, max = 4)
    private String expiryDate; //유효기간(4자리 숫자, mmyy)

    @NotNull
    @Size(min = 3, max = 3)
    private String cvcNum;     //cvc(3자리 숫자)

    @NotNull
    @Min(0) @Max(12)
    private Integer installments;   //할부개월수 : 0(일시불), 1 ~ 12

    @NotNull
    @Min(100) @Max(1000000000)
    private Integer amount;  //결제금액(100원 이상, 10억원 이하, 숫자)

    private Integer vat;      //부가가치세 optional
}
