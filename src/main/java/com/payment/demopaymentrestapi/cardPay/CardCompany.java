package com.payment.demopaymentrestapi.cardPay;

import com.payment.demopaymentrestapi.common.BaseTimeEntity;
import lombok.*;
import org.hibernate.validator.constraints.CodePointLength;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity @Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class CardCompany extends BaseTimeEntity {
    @Id @GeneratedValue
    private Long seqNum;
    @Column(length = 450)
    private String data;
}
