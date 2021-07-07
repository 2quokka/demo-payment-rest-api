package com.payment.api.payment.entity;

import com.payment.api.common.BaseTimeEntity;
import com.payment.api.payment.dto.State;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import javax.persistence.*;
import java.time.LocalDateTime;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "paymentId", callSuper = false)
@Entity
@Table
public class PaymentInfo extends BaseTimeEntity { //생성일시 수정일시 자동생성

    @Id @Column(name = "PAYMENT_ID", unique = true, length = 20, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id")
    @GenericGenerator(
            name="seq_id",
            strategy = "com.payment.api.payment.entity.IdGenerator",
            parameters = {
                    @Parameter(name = IdGenerator.INCREMENT_PARAM, value = "50"),
                    @Parameter(name = IdGenerator.VALUE_PREFIX_PARAMETER, value = "T_"),
                    @Parameter(name = IdGenerator.NUMBER_FORMAT_PARAMETER, value = "%018d"),
            }
    )
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
