package com.payment.api.payment.repository;

import com.payment.api.payment.entity.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, String> {
    Optional<PaymentInfo> findByPaymentId(String id);
}
