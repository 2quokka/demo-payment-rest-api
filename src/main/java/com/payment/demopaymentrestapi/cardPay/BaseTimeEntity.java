package com.payment.demopaymentrestapi.cardPay;

import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dataCreateTime;  //데이터생성일시

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dataModifyTime;  //데이터변경일시
}
