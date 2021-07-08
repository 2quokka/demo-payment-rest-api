package com.payment.api.payment.entity;

import com.payment.api.common.BaseTimeEntity;
import com.payment.api.payment.dto.State;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "paymentId", callSuper = false)
@Entity
@Table @ToString
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

    @NotNull
    @Column(length = 300)
    private String encCardInfo;    //카드정보 암호화

    @Column(length = 2)
    private String installments;   //할부개월수 : 00(일시불), 1 ~ 12

    @Column
    private Integer amount;  //결제금액(100원 이상, 10억원 이하, 숫자)

    @Column
    private Integer vat;     //부가가치세

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private State state;      //상태코드

    @Column
    private Integer finalAmount; //최종결제금액

    @Column
    private Integer finalVat; //최종부가가치세

    @Column(length = 20)
    private String orginPaymentId; //원래 관리번호

    @Column
    private LocalDateTime approvalTime;  //결제승인일시

    @Column
    private LocalDateTime cancleTime; //결제취소일시

}
