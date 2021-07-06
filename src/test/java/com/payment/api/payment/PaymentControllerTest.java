package com.payment.api.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.api.common.TestDescription;
import com.payment.api.payment.dto.CancelPaymentDTO;
import com.payment.api.payment.dto.PaymentIdDTO;
import com.payment.api.payment.entity.PaymentInfo;
import com.payment.api.payment.dto.PaymentInfoDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @TestDescription("결제요청시 정상 동작 테스트")
    public void payment_correct_input() throws Exception {
        PaymentInfoDTO paymentInfoDTO = PaymentInfoDTO.builder()
                .cardNum("1234567890123456")
                .expiryDate("1234")
                .cvcNum("123")
                .installments(12)
                .amount(1000)
                .vat(100)
                .build();

        mockMvc.perform(post("/api/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentInfoDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
        ;
    }

    @Test
    @TestDescription("결제요청시 잘못된 키가 들어왔을 경우")
    public void payment_bad_request_wrong_property() throws Exception {
        PaymentInfo paymentInfo = PaymentInfo.builder()
                .paymentId("123213") //관리번호가 request로 들어왔을때, BadRequest
                .cardNum("123123123123123")
                .expiryDate("0124")
                .cvcNum("123")
                .installments("12")
                .amount(10000)
                .build();

        this.mockMvc.perform(post("/api/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(paymentInfo)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
        ;
    }

    @Test
    @TestDescription("결제요청시 입력 값이 안 들어온 경우 ")
    public void payment_bad_request_input_null() throws Exception {
        PaymentInfoDTO paymentInfoDTO = PaymentInfoDTO.builder().build();

        this.mockMvc.perform(post("/api/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(paymentInfoDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
        ;
    }

    @Test
    @TestDescription("결제요청시 입력 값이 잘못된 경우 ")
    public void payment_bad_request_wrong_value() throws Exception {
        PaymentInfoDTO paymentInfoDTO = PaymentInfoDTO.builder()
                .cardNum("123")     //잘못된 카드넘버
                .expiryDate("12")
                .cvcNum("123")
                .installments(12)
                .amount(10000)
                .build();

        this.mockMvc.perform(post("/api/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(paymentInfoDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
        ;
    }

    @Test
    @TestDescription("결제 취소시 정상 처리 응답")
    public void cancel_correct_input() throws Exception {
        CancelPaymentDTO cancelPaymentDTO = CancelPaymentDTO.builder()
                .paymentId("00000000000000000001")
                .cancelAmount(1000)
                .cancelVat(100)
                .build();

        this.mockMvc.perform(post("/api/cancel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(cancelPaymentDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
        ;
    }

    @Test
    @TestDescription("결제 조회 정상 처리 응답")
    public void payment_search_correct_input() throws Exception {
        PaymentIdDTO paymentIdDTO = PaymentIdDTO.builder()
                .paymentId("00000000000000000001")
                .build();

        this.mockMvc.perform(get("/api/payment-info")
                .param("paymentId", "00000000000000000001"))
                .andDo(print())
                .andExpect(status().isCreated())
        ;
    }
}
