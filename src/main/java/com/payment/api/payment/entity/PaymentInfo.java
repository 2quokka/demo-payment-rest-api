package com.payment.api.payment.entity;

import com.payment.api.common.BaseTimeEntity;
import com.payment.api.payment.dto.State;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "paymentId", callSuper = false)
@Entity
@Table
public class PaymentInfo extends BaseTimeEntity { //생성일시 수정일시 자동생성

    @Id @Column(name = "PAYMENT_ID", unique = true, length = 20, nullable = false)
    @GenericGenerator(name="seq_payment_id", strategy = "com.payment.api.payment.entity.IdGenerator")
    @GeneratedValue(generator = "seq_payment_id")
    private String paymentId; //관리번호(unique id, 20자리)

    @Column(length = 16)
    private String cardNum; //카드번호(10 ~ 16자리 숫자)

    @Column(length = 4)
    private String expiryDate; //유효기간(4자리 숫자, mmyy)

    @Column(length = 3)
    private String cvcNum;     //cvcNum(3자리 숫자)

    @Column(length = 2)
    private String installments;   //할부개월수 : 00(일시불), 1 ~ 12

    @Column
    private Integer amount;  //결제금액(100원 이상, 10억원 이하, 숫자)

    @Column
    private Integer vat;     //부가가치세

    @Column
    private Integer finalAmount;     //최종결제금액

    @Column
    private Integer finalVat;        //최종부가가치세

    @Enumerated(EnumType.ORDINAL)
    private State status;      //상태코드

    private LocalDateTime approvalTime;  //결제승인일시

//    @OneToMany(mappedBy = "paymentInfo")
//    private List<CancelPayment> cancelPaymentList = new ArrayList<>();
}
