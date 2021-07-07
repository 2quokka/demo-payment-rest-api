package com.payment.api.payment.dto;

import com.payment.api.card.CardInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@Builder @Data @AllArgsConstructor @NoArgsConstructor
public class SearchResponse {
    private String paymentId;
    private CardInfo cardInfo;
    private State state;
    private Integer amount;
    private Integer vat;
}
