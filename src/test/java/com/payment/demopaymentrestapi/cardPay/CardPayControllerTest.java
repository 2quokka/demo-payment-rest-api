package com.payment.demopaymentrestapi.cardPay;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CardPayControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void createCardPayInfo() throws Exception {
        CardPayInfoDTO cardPayInfoDTO = CardPayInfoDTO.builder()
                .cardNum("123123213")
                .expDate("0124")
                .cvc("123")
                .payPlanMonth(12)
                .amount(10000)
                .build();

        mockMvc.perform(post("/api/paycard")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardPayInfoDTO))
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
