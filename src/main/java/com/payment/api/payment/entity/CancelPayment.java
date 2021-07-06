package com.payment.api.payment.entity;

import com.payment.api.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity @Builder
@Getter @Service
@AllArgsConstructor @NoArgsConstructor
@Table
public class CancelPayment extends BaseTimeEntity implements Serializable {

    @Id
    @JoinColumn (name = "PAYMENT_ID")
    @ManyToOne
    private PaymentInfo paymentInfo;

    @Id @GeneratedValue
    private Long seqNum;

    @Column
    private int cancelAmount;

    @Column
    private int cancelVat;

    @Column
    private LocalDateTime cancelTime;

}
