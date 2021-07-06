package com.payment.api.payment.entity;

import com.payment.api.common.BaseTimeEntity;
import com.payment.api.payment.dto.State;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "paymentId", callSuper = false)
@Entity
@Table
public class PaymentInfo extends BaseTimeEntity { //생성일시 수정일시 자동생성

    @Id @Column(name = "PAYMENT_ID", unique = true, length = 20, nullable = false)
    private String paymentId; //관리번호(unique id, 20자리)

    @Column(length = 16)
    private String cardNum; //카드번호(10 ~ 16자리 숫자)

    @Column(length = 4)
    private String expiryDate; //유효기간(4자리 숫자, mmyy)

    @Column(length = 3)
    private String cvcNum;     //cvcNum(3자리 숫자)

    private int installments;   //할부개월수 : 0(일시불), 1 ~ 12

    private int amount;  //결제금액(100원 이상, 10억원 이하, 숫자)

    private int vat;     //부가가치세

    private int finalAmount;     //최종결제금액

    private int finalVat;        //최종부가가치세

    @Enumerated(EnumType.ORDINAL)
    private State status;      //상태코드

    private LocalDateTime approvalTime;  //결제승인일시
}
