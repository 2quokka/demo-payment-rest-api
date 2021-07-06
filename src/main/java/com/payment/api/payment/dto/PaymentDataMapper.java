package com.payment.api.payment.dto;

import com.payment.api.common.DataOfCardCompany;
import com.payment.api.payment.entity.PaymentInfo;
import org.modelmapper.PropertyMap;

public class PaymentDataMapper extends PropertyMap<PaymentInfo, DataOfCardCompany> {
    @Override
    protected void configure() {
        map().setPaymentType("PAYMENT");
        map().setPaymentId(source.getPaymentId());
        map().setCardNum(source.getCardNum());
        map().setInstallment(String.valueOf(source.getInstallments()));
        map().setExpiryDate(source.getExpiryDate());
        map().setCvcNum(source.getCvcNum());
        map().setAmount(String.valueOf(source.getAmount()));
        map().setVat(String.valueOf(source.getVat()));
    }
}
