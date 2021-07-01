package com.payment.demopaymentrestapi.cardPay;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
public class CardPayController {

    @PostMapping(value = "/api/paycard", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createCardPayInfo(@RequestBody CardPayInfo cardPayInfo){
        URI createURI = linkTo(methodOn(CardPayController.class).createCardPayInfo(null)).slash("{cardNum}").toUri();
        return ResponseEntity.created(createURI).body(cardPayInfo);
    }

}
