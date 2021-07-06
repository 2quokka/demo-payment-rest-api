package com.payment.api.payment.service;

import com.payment.api.common.TestDescription;
import com.payment.api.exception.PaymentNotFoundException;
import com.payment.api.payment.dto.CancelPaymentDTO;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentServiceTest {

    @Autowired
    PaymentService paymentService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    @TestDescription("취소 요청시 결제 건이 없을 경우 ")
    public void cancel_not_found(){
        CancelPaymentDTO cancelPaymentDTO = CancelPaymentDTO.builder()
                .paymentId("99999999999999999999")
                .build();

        expectedException.expect(PaymentNotFoundException.class);
    }
}