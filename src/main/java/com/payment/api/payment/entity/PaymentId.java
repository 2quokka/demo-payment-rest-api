package com.payment.api.payment.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@SequenceGenerator(
        name="PAYMENT_ID_SEQ_GEN", //시퀀스 제너레이터 이름
        sequenceName="PAYMENT_ID_SEQ", //시퀀스 이름
        initialValue=1, //시작값
        allocationSize=1 //메모리를 통해 할당할 범위 사이즈
)
@Getter @NoArgsConstructor
public class PaymentId {
    @Id  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_ID_SEQ_GEN")
    private Long id;
}
