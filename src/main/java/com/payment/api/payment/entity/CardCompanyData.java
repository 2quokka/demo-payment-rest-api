package com.payment.api.payment.entity;

import com.payment.api.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity @Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Table (name = "CARD_COMPANY_DATA")
public class CardCompanyData extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long seqNum;

    @Column(length = 450)
    private String data;
}
