package com.payment.api.payment.repository;

import com.payment.api.payment.entity.CardCompanyData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardCompanyDataRepository extends JpaRepository<CardCompanyData, Long> {

}