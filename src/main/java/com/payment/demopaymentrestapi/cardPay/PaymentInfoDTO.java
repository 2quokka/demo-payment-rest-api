package com.payment.demopaymentrestapi.cardPay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

/*
* 전달 받고자 하는 데이터만 선언한 클래스
* 요청받을때 사용.
* */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PaymentInfoDTO {
    private Long cardNum; //카드번호(10 ~ 16자리 숫자)
    private Integer expiryDate; //유효기간(4자리 숫자, mmyy)
    private Integer cvcNum;     //cvc(3자리 숫자)
    private Integer installments;   //할부개월수 : 0(일시불), 1 ~ 12
    private Integer amount;  //결제금액(100원 이상, 10억원 이하, 숫자)
    private Integer vat;     //부가가치세
}
