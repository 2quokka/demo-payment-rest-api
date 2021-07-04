package com.payment.demopaymentrestapi.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Slf4j
public class SecCardInfoTest {

    @Test
    @TestDescription("SEED 암호화방식을 이용한 카드정보 암복호화 테스트")
    public void enc_dec_test(){
        SecCardInfo secCardInfo = SecCardInfo.builder()
                .cardNum("123123123")
                .expiryDate("1223")
                .cvc("123")
                .build();


        String enc = secCardInfo.encCardInfo();
        log.info("암호화 카드정보 : " + enc);

        String dec = SecCardInfo.decCardInfo(enc);
        log.info("복호화 카드정보 : " + dec);

        SecCardInfo decCardInfo = SecCardInfo.getCardInfo(dec);
        log.info(decCardInfo.toString());

        assertThat(decCardInfo.getCardNum()).isEqualTo(secCardInfo.getCardNum());
        assertThat(decCardInfo.getExpiryDate()).isEqualTo(secCardInfo.getExpiryDate());
        assertThat(decCardInfo.getCvc()).isEqualTo(secCardInfo.getCvc());
    }
}