package com.payment.api.payment.repository;

import com.payment.api.payment.entity.CancelPayment;
import com.payment.api.payment.entity.CancelPaymentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancelPaymentRepository extends JpaRepository<CancelPayment, CancelPaymentId> {
}
