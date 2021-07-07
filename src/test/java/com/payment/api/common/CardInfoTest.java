package com.payment.api.common;

import com.payment.api.card.CardInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Slf4j
public class CardInfoTest {

    @Test
    @TestDescription("SEED 암호화방식을 이용한 카드정보 암복호화 테스트")
    public void enc_dec_test(){
        CardInfo cardInfo = CardInfo.builder()
                .cardNum("123123123")
                .expiryDate("1223")
                .cvc("123")
                .build();


        String enc = cardInfo.encCardInfo();
        log.info("암호화 카드정보 : " + enc);

        CardInfo decCardInfo = CardInfo.getDecCardInfo(enc);
        log.info("복호화 카드정보 : " + decCardInfo.toString());

        assertThat(decCardInfo.getCardNum()).isEqualTo(cardInfo.getCardNum());
        assertThat(decCardInfo.getExpiryDate()).isEqualTo(cardInfo.getExpiryDate());
        assertThat(decCardInfo.getCvc()).isEqualTo(cardInfo.getCvc());
    }
}