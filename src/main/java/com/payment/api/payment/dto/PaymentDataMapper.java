package com.payment.api.payment.dto;

import com.payment.api.common.GenDataForForwarding;
import com.payment.api.payment.entity.PaymentInfo;
import org.modelmapper.PropertyMap;

public class PaymentDataMapper extends PropertyMap<PaymentInfo, GenDataForForwarding> {
    @Override
    protected void configure() {
        map().setPaymentId(source.getPaymentId());
        map().setInstallment(source.getInstallments());
        map().setEncCardInfo(source.getEncCardInfo());
        map().setAmount(String.valueOf(source.getAmount()));
        map().setVat(String.valueOf(source.getVat()));
    }
}

