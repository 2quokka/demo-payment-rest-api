package com.payment.demopaymentrestapi.cardPay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.demopaymentrestapi.common.TestDescription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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
                .amount(10000)
                .vat(0)
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
    @TestDescription("결제요청시 잘못된 키가 들어왔을 경우 무시하고 올바른 키만 처리 ")
    public void payment_bad_request_wrong_property() throws Exception {
        //관리번호가 request로 들어왔을때, BadRequest
        PaymentInfo paymentInfo = PaymentInfo.builder()
                .paymentId("123213")
                .cardNum("123123123123123")
                .expiryDate("0124")
                .cvcNum("123")
                .installments(12)
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
    @TestDescription("결제요청시 입력 값이 안들어온 경우 ")
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
}
