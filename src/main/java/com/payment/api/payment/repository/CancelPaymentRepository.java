package com.payment.api.payment.repository;

import com.payment.api.payment.entity.CancelPayment;
import com.payment.api.payment.entity.CancelPaymentId;
import com.payment.api.payment.entity.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CancelPaymentRepository extends JpaRepository<CancelPayment, CancelPaymentId> {
    @Query(value = "select count(c)+1 as cnt from CancelPayment c where c.paymentId=:paymentId")
    long getSeq (@Param("paymentId") PaymentInfo paymentId);
}