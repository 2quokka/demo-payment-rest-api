package com.payment.api.payment;

import com.payment.api.common.TestDescription;
import com.payment.api.payment.entity.PaymentInfo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
class PaymentInfoTest {

    @Test
    @TestDescription("")
    public void builder() {
        PaymentInfo paymentInfo = PaymentInfo.builder().build();
        assertThat(paymentInfo).isNotNull();
    }
}