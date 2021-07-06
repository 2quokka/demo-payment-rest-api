package com.payment.api.payment.dto;

import com.payment.api.payment.entity.PaymentInfo;
import org.modelmapper.PropertyMap;

public class PaymentInfoMapper extends PropertyMap<PaymentInfoDTO, PaymentInfo> {

    @Override
    protected void configure() {
//        map().setCardNum(source.getCardNum().toString());
//        map().setExpiryDate(source.getExpiryDate().toString());
//        map().setCvcNum(source.getCvcNum().toString());
        map().setInstallments(String.format("%02d", source.getInstallments())); // 할부 00 : 일시불 ~ 2자리
        map().setAmount(source.getAmount());
        map().setVat(source.getVat());
    }
}
