package com.payment.demopaymentrestapi.mapper;

import com.payment.demopaymentrestapi.payment.PaymentInfo;
import com.payment.demopaymentrestapi.payment.PaymentInfoDTO;
import org.modelmapper.PropertyMap;

public class PaymentInfoMapper extends PropertyMap<PaymentInfoDTO, PaymentInfo> {

    @Override
    protected void configure() {
        map().setCardNum(source.getCardNum().toString());
        map().setExpiryDate(source.getExpiryDate().toString());
        map().setCvcNum(source.getCvcNum().toString());
        map().setInstallments(source.getInstallments());
        map().setAmount(source.getAmount());
        map().setVat(source.getVat());
    }
}
