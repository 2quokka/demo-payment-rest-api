package com.payment.api.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.api.exception.PaymentNotFoundException;
import com.payment.api.payment.dto.CancelPaymentDTO;
import com.payment.api.payment.dto.PaymentInfoDTO;
import com.payment.api.payment.entity.PaymentInfo;
import com.payment.api.payment.repository.PaymentInfoRepository;
import com.payment.api.payment.service.PaymentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MultiThreadTest {
    private static final ExecutorService service =
            Executors.newFixedThreadPool(100);

    @Autowired
    private PaymentService paymentService;

    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private PaymentInfoRepository paymentInfoRepository;

    @Before
    public void setUp() throws Exception{
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .build();

        PaymentInfoDTO paymentInfoDTO = PaymentInfoDTO.builder()
                .amount(10000)
                .vat(0)
                .cardNum("1234567890123456")
                .cvcNum("123")
                .installments(10)
                .expiryDate("1111")
                .build();

        mockMvc.perform(post("/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentInfoDTO)));
    }


    @Test
    //전체 취소, 부분취소시, 스레드간 동시성 제어
    //Id로 결제 정보 조회시 pessimistic lock mode를 사용하여 순차처리하도록함.
    public void cancelPayment_multiThread_Check() throws Exception {
        int numberOfThreads = 100;
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        //given
        CancelPaymentDTO cancelPaymentDTO = CancelPaymentDTO.builder()
                .paymentId("T_000000000000000001")
                .cancelAmount(100)
                .cancelVat(0)
                .build();

        //when
        for (int i = 0; i < numberOfThreads; i++) {
            service.execute(() -> {
                try {
                    this.mockMvc.perform(post("/cancel-payment")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(this.objectMapper.writeValueAsString(cancelPaymentDTO)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                latch.countDown();
            });
        }
        latch.await();

        //then
        PaymentInfo paymentInfo = paymentInfoRepository.findById("T_000000000000000001")
                .orElseThrow(() -> new PaymentNotFoundException("T_000000000000000001"));

        Integer amount = paymentInfo.getFinalAmount();

        assertEquals(Integer.valueOf(0), amount);
    }
}
