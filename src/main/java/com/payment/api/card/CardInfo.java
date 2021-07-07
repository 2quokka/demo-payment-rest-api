package com.payment.api.card;

import com.payment.api.common.SeedCrypto;
import lombok.*;

import java.util.StringJoiner;

@Builder @Getter @Setter
@NoArgsConstructor @ToString
public class CardInfo {
    private String cardNum = "";
    private String expiryDate = "";
    private String cvc = "";
    public static String delimiter = "_";

    public CardInfo(String cardNum, String expiryDate, String cvc){
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
        문자열을 구분자로 구분하여 카드정보를 셋팅하여 반환한다.
    */
    public static CardInfo getDecCardInfo(String encCardInfo){
        String param[] = new String[3];
        String decCardInfo = SeedCrypto.decrypt(encCardInfo);
        String[] infos = decCardInfo.split(delimiter);

        int i = 0;
        for (String p : infos) {
            param[i++]=p;
        }

        return new CardInfo(param[0], param[1], param[2]);

    }
}
