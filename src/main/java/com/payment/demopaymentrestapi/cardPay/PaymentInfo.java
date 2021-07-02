package com.payment.demopaymentrestapi.cardPay;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;

    @Builder @AllArgsConstructor @NoArgsConstructor
    @Getter @Setter
    @EqualsAndHashCode(of = "paymentId")
    @Entity
    public class PaymentInfo extends BaseTimeEntity { //생성일시 수정일시 자동생성

    @Id
    private Long paymentId; //관리번호(unique id, 20자리)
    private String cardNum; //카드번호(10 ~ 16자리 숫자)
    private String expiryDate; //유효기간(4자리 숫자, mmyy)
    private String cvcNum;     //cvcNum(3자리 숫자)
    private int installments;   //할부개월수 : 0(일시불), 1 ~ 12
    private int amount;  //결제금액(100원 이상, 10억원 이하, 숫자)
    private int vat;     //부가가치세
    private int finalAmount;     //최`종결제금액
    private int finalVat;        //최종부가가치세
    private String status;      //상태코드(00:승인, 01:전체취소, 02:부분취소)

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime approvalTime;  //결제승인일시

}
