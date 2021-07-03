package com.payment.demopaymentrestapi.cardPay;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
class PaymentInfoTest {
    @Test
    public void builder() {
        PaymentInfo paymentInfo = PaymentInfo.builder().build();
        assertThat(paymentInfo).isNotNull();
    }
}