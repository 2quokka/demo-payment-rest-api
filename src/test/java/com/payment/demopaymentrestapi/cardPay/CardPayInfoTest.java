package com.payment.demopaymentrestapi.cardPay;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CardPayInfoTest {

    @Test
    public void builder(){
        CardPayInfo cardPayInfo = CardPayInfo.builder().build();
        assertThat(cardPayInfo).isNotNull();
    }
}