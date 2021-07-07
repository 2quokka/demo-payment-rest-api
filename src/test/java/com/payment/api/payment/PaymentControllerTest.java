package com.payment.api.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.api.common.TestDescription;
import com.payment.api.payment.dto.CancelPaymentDTO;
import com.payment.api.payment.dto.PaymentIdDTO;
import com.payment.api.payment.dto.PaymentInfoDTO;
import com.payment.api.payment.service.PaymentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentControllerTest {

    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PaymentService paymentService;
    
    @Before
    public void before() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .build();

        PaymentInfoDTO paymentInfoDTO_1 = PaymentInfoDTO.builder()
                .amount(11000)
                .vat(1000)
                .cardNum("1234567890123456")
                .cvcNum("123")
                .installments(10)
                .expiryDate("1111")
                .build();
        PaymentInfoDTO paymentInfoDTO_2 = PaymentInfoDTO.builder()
                .amount(20000)
                .vat(909)
                .cardNum("1234567890123456")
                .cvcNum("123")
                .installments(10)
                .expiryDate("1111")
                .build();
        PaymentInfoDTO paymentInfoDTO_3 = PaymentInfoDTO.builder()
                .amount(20000)
                .cardNum("1234567890123456")
                .cvcNum("123")
                .installments(10)
                .expiryDate("1111")
                .build();

        mockMvc.perform(post("/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentInfoDTO_1)));
        mockMvc.perform(post("/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentInfoDTO_2)));
        mockMvc.perform(post("/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentInfoDTO_3)));
    }

    /*
    * 1. 카드결제 API 테스트
    * */
    @Test
    @TestDescription("결제요청시 정상 응답 테스트")
    public void 카드결제_정상응답() throws Exception {

        PaymentInfoDTO paymentInfoDTO = PaymentInfoDTO.builder()
                .cardNum("1234567890123456")
                .expiryDate("1234")
                .cvcNum("123")
                .installments(12)
                .amount(1000)
                .vat(100)
                .build();

        mockMvc.perform(post("/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentInfoDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("paymentId").exists())
                .andExpect(jsonPath("data").exists())
        ;
    }

    @Test
    @TestDescription("결제요청시 입력 값이 안 들어온 경우 ")
    public void 카드결제_입력값_없음() throws Exception {
        PaymentInfoDTO paymentInfoDTO = PaymentInfoDTO.builder().build();

        this.mockMvc.perform(post("/payment")
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
    public void 카드결제_잘못된_입력값() throws Exception {
        PaymentInfoDTO paymentInfoDTO = PaymentInfoDTO.builder()
                .cardNum("123")     //잘못된 카드넘버
                .expiryDate("12")
                .cvcNum("123")
                .installments(12)
                .amount(10000)
                .build();

        this.mockMvc.perform(post("/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(paymentInfoDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
        ;
    }

    /*
     * 2. 카드취소 API 테스트
     * */
    @Test
    @TestDescription("결제취소시 정상응답")
    public void 결제취소_정상_응답() throws Exception {
        CancelPaymentDTO cancelPaymentDTO = CancelPaymentDTO.builder()
                .paymentId("T_000000000000000001")
                .cancelAmount(1000)
                .cancelVat(100)
                .build();

        this.mockMvc.perform(post("/cancel-payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(cancelPaymentDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
        ;
    }

    @Test
    @TestDescription("결제취소시 입력 값이 안 들어온 경우 ")
    public void 결제취소_입력값_없음() throws Exception {
        CancelPaymentDTO cancelPaymentDTO = CancelPaymentDTO.builder().build();

        this.mockMvc.perform(post("/cancel-payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(cancelPaymentDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
        ;
    }

    @Test
    @TestDescription("결제취소시 입력 값이 잘못된 경우 ")
    public void 결제취소_잘못된_입력값() throws Exception {
        CancelPaymentDTO cancelPaymentDTO = CancelPaymentDTO.builder()
                .paymentId("T_000000000000000001")
                .cancelAmount(1)
                .build();

        this.mockMvc.perform(post("/cancel-payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(cancelPaymentDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
        ;
    }

    /*
     * 3. 결제조회 API 테스트
     * */
    @Test
    @TestDescription("결제조회시 정상응답")
    public void 결제조회_정상_응답() throws Exception {

        this.mockMvc.perform(get("/payment-info")
                .param("paymentId","T_000000000000000001")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("paymentId").exists())
                .andExpect(jsonPath("cardInfo").exists())
                .andExpect(jsonPath("amount").exists())
                .andExpect(jsonPath("state").exists())
                .andExpect(jsonPath("vat").exists())
        ;
    }

    @Test
    @TestDescription("결제조회시 입력 값이 안 들어온 경우 ")
    public void 결제조회_입력값_없음() throws Exception {
        this.mockMvc.perform(get("/payment-info")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
        ;
    }

    @Test
    @TestDescription("결제조회시 입력 값이 잘못된 경우 ")
    public void 결제조회_잘못된_입력값() throws Exception {
        this.mockMvc.perform(get("/payment-info")
                .param("paymentId","gg0012930129049210124")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").exists())
                .andExpect(jsonPath("message").exists())
        ;
    }

}
