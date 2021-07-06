package com.payment.api.payment.entity;

import com.payment.api.common.BaseTimeEntity;
import lombok.*;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Service
@AllArgsConstructor @NoArgsConstructor
@Table @IdClass(CancelPaymentId.class)
public class CancelPayment extends BaseTimeEntity {

    @Id
    @JoinColumn (name = "PAYMENT_ID")
    @ManyToOne
    private PaymentInfo paymentId;

    @Id @GeneratedValue
    private Long seqNum;

    @Column
    private Integer cancelAmount;

    @Column
    private Integer cancelVat;

    @Column
    private LocalDateTime cancelTime;

}
