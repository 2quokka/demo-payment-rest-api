package com.payment.demopaymentrestapi.cardPay;

import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
public class CardPayController {

    private final ModelMapper modelMapper;

    private final CardPayRepository cardPayRepository;

    public CardPayController(ModelMapper modelMapper, CardPayRepository cardPayRepository) {
        this.modelMapper = modelMapper;
        this.cardPayRepository = cardPayRepository;
    }

    @PostMapping(value = "/api/paycard", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createCardPayInfo(@RequestBody CardPayInfoDTO cardPayInfoDTO){
        CardPayInfo cardPayInfo = modelMapper.map(cardPayInfoDTO, CardPayInfo.class);
        CardPayInfo saveCardPayInfo = this.cardPayRepository.save(cardPayInfo);
        URI createURI = linkTo(methodOn(CardPayController.class).createCardPayInfo(null)).slash(cardPayInfo.getId()).toUri();
        return ResponseEntity.created(createURI).body(cardPayInfo);
    }

}
