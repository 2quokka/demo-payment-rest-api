package com.payment.demopaymentrestapi.cardPay;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
@Entity
public class CardPayInfo {
    @Id
    @GeneratedValue
    private Long id; //관리번호(unique id, 20자리)
    private String cardNum; //카드번호(10 ~ 16자리 숫자)
    private String expDate; //유효기간(4자리 숫자, mmyy)
    private String cvc;     //cvc(3자리 숫자)
    private int payPlanMonth;   //할부개월수 : 0(일시불), 1 ~ 12
    private int amount;  //결제금액(100원 이상, 10억원 이하, 숫자)
    private int vat;     //부가가치세
}

