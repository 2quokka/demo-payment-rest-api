package com.payment.api.payment.repository;

import com.payment.api.payment.entity.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, String> {
    Optional<PaymentInfo> findById(String id);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select p from PaymentInfo p where p.paymentId = :id")
    Optional<PaymentInfo> findByIdForUpdate(@Param("id") String id);
}
