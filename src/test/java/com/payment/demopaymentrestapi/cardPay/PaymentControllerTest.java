package com.payment.demopaymentrestapi.cardPay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    /*
        1.결제 API 테스트
    * */

    //정상
    @Test
    public void payment() throws Exception {
        PaymentInfoDTO paymentInfoDTO = PaymentInfoDTO.builder()
                .cardNum(123123213213L)
                .expiryDate(12)
                .cvcNum(123)
                .installments(12)
                .amount(10000)
                .build();

        mockMvc.perform(post("/api/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentInfoDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("paymentId").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
        ;
    }

    //BADRequest
    @Test
    public void payment_BADRequest() throws Exception {

        //관리번호가 request로 들어왔을때 실패를 반환한다.
        PaymentInfo paymentInfo = PaymentInfo.builder()
                .paymentId("123213")
                .cardNum("123123213")
                .expiryDate("0124")
                .cvcNum("123")
                .installments(12)
                .amount(10000)
                .build();

        mockMvc.perform(post("/api/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentInfo)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    /*
    * 예상 밖이의 요청값인경우 처리
    * */
    @Test
    public void payment_Bad_request() throws Exception {
        PaymentInfoDTO paymentInfoDTO = PaymentInfoDTO.builder().build();

        mockMvc.perform(post("/api/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(paymentInfoDTO)))
                .andExpect(status().isBadRequest())
        ;
    }
    /*
    @Test
    public void testBaseTimeEntity(){
        LocalDateTime now =  LocalDateTime.of(2021, 7,2, 0,0,0);
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
    */
}
