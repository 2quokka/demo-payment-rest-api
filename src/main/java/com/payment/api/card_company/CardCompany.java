package com.payment.api.card_company;

import com.payment.api.common.BaseTimeEntity;
import lombok.*;

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
