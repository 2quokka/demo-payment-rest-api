package com.payment.demopaymentrestapi.cardPay;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CardPayRepository extends JpaRepository<CardPayInfo, Long> {
}
