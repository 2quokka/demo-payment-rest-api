package com.payment.demopaymentrestapi.cardPay;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class CardPayControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void createCardPayInfo() throws Exception {
        CardPayInfo cardPayInfo = CardPayInfo.builder()
                .cardNum("123123213")
                .expDate("0124")
                .cvc("123")
                .payPlanMonth(12)
                .amount(BigDecimal.valueOf(10000))
                .build();

        mockMvc.perform(post("/api/paycard")
                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaTypes.)
                .content(objectMapper.writeValueAsString(cardPayInfo))
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
