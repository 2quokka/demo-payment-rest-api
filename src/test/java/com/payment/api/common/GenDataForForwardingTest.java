package com.payment.api.common;

import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GenDataForForwardingTest {

    @Test
    @TestDescription("결제 String 데이터 ")
    public void gen_payment_string(){
        GenDataForForwarding genDataForForwarding = GenDataForForwarding.builder()
                .cardNum("1234567890123456")
                .expiryDate("1125")
                .cvcNum("777")
                .installment("0")
                .amount("110000")
                .vat("10000")
                .encCardInfo("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY\n" +
                        "YYYYYYYYYYYYYYYYYYYYYYYYYYYYY")
                .paymentId("XXXXXXXXXXXXXXXXXXXX")
                .paymentType("PAYMENT")
                .build();

        String data = genDataForForwarding.generateData();
        assertThat(data).isNotNull();

    }

    @Test
    @TestDescription("전체취소 String 데이터 ")
    public void gen_cancel_string(){
        GenDataForForwarding genDataForForwarding = GenDataForForwarding.builder()
                .cardNum("1234567890123456")
                .expiryDate("1125")
                .cvcNum("777")
                .installment("0")
                .amount("110000")
                .vat("10000")
                .encCardInfo("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY\n" +
                        "YYYYYYYYYYYYYYYYYYYYYYYYYYYYY")
                .paymentId("ZZZZZZZZZZZZZZZZZZZZ")
                .orginPaymentId("XXXXXXXXXXXXXXXXXXXX")
                .paymentType("CANCEL")
                .build();

        String data = genDataForForwarding.generateData();
        assertThat(data).isNotNull();

    }
}