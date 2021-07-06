package com.payment.api.payment.dto;

import com.payment.api.common.GenDataForForwarding;
import com.payment.api.payment.entity.PaymentInfo;
import org.modelmapper.PropertyMap;

public class PaymentDataMapper extends PropertyMap<PaymentInfo, GenDataForForwarding> {
    @Override
    protected void configure() {
        map().setPaymentId(source.getPaymentId());
        map().setCardNum(source.getCardNum());
        map().setInstallment(source.getInstallments());
        map().setExpiryDate(source.getExpiryDate());
        map().setCvcNum(source.getCvcNum());
        map().setAmount(String.valueOf(source.getAmount()));
        map().setVat(String.valueOf(source.getVat()));
    }
}

