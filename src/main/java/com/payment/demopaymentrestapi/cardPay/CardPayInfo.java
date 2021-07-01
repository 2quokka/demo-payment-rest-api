package com.payment.demopaymentrestapi.cardPay;

import lombok.*;

import java.math.BigDecimal;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class CardPayInfo {
    private String cardNum; //카드번호(10 ~ 16자리 숫자)
    private String expDate; //유효기간(4자리 숫자, mmyy)
    private String cvc;     //cvc(3자리 숫자)
    private Integer payPlanMonth;   //할부개월수 : 0(일시불), 1 ~ 12
    private BigDecimal amount;  //결제금액(100원 이상, 10억원 이하, 숫자)
    private BigDecimal vat;     //부가가치세
}

