package com.payment.api.common;

import lombok.*;

import java.util.StringJoiner;

@Builder @Getter @Setter
@NoArgsConstructor @ToString
public class SecCardInfo {
    private String cardNum = "";
    private String expiryDate = "";
    private String cvc = "";
    public static String delimiter = "_";

    public SecCardInfo(String cardNum, String expiryDate, String cvc){
        this.cardNum = cardNum;
        this.expiryDate = expiryDate;
        this.cvc = cvc;
    }

    /*
        카드정보를 암호화하여 문자열로 반환한다.
     */
    public static String encCardInfo(String cardNum, String expiryDate, String cvc){
        StringJoiner sj = new StringJoiner(delimiter);
        sj.add(cardNum);
        sj.add(expiryDate);
        sj.add(cvc);

        return SeedCrypto.encrypt(sj.toString());
    }


    public String encCardInfo(){
        return encCardInfo(this.cardNum, this.expiryDate, this.cvc);
    }

    /*
        카드정보를 복호화하여 문자열로 반환한다.
     */
    public static String decCardInfo(String enc){
        return SeedCrypto.decrypt(enc);
    }

    /*
        문자열을 구분자로 구분하여 카드정보를 셋팅하여 반환한다.
    */
    public static SecCardInfo getCardInfo(String cardInfo){
        String[] infos = cardInfo.split(delimiter);
        return new SecCardInfo(infos[0], infos[1], infos[2]);
    }
}
