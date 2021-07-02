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

    private final PaymentInfoRepository paymentInfoRepository;

    PaymentInfoTest(PaymentInfoRepository paymentInfoRepository) {
        this.paymentInfoRepository = paymentInfoRepository;
    }


    @Test
    public void builder(){
        PaymentInfo paymentInfo = PaymentInfo.builder().build();
        assertThat(paymentInfo).isNotNull();
    }

    @Test
    public void testBaseTimeEntity(){
        LocalDateTime now = LocalDateTime.of(2021, 7,2, 0,0,0);
        paymentInfoRepository.save(PaymentInfo.builder()
                .paymentId(1L)
                .amount(100)
                .cardNum("123213")
                .finalAmount(100)
                .expiryDate("0221")
                .vat(100)
                .finalVat(100)
                .installments(3)
                .cvcNum("123")
                .build());

        PaymentInfo paymentInfo = paymentInfoRepository.findAll().get(0);

        System.out.println(">>>>>>> createDate="+paymentInfo.getDataCreateTime()+", modifiedDate="+paymentInfo.getDataModifyTime());

        assertThat(paymentInfo.getDataCreateTime()).isAfter(now);
        assertThat(paymentInfo.getDataModifyTime()).isAfter(now);

    }
}