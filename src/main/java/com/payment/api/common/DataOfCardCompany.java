package com.payment.api.common;

import lombok.*;

@Builder @Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class DataOfCardCompany {
    private final int LEFT_SPACE = 0; //숫자
    private final int LEFT_ZERO = 1 ; //숫자(0)
    private final int RIGHT_SPACE = 2; //숫자(L), 문자

    private String dataLength;
    private String paymentType;
    private String paymentId;
    private String cardNum;
    private String installment;
    private String expiryDate;
    private String cvcNum;
    private String amount;
    private String vat;
    private String orginPaymentId; //취소시에만 결제관리번호 저장, 결제시에는 공백
    private String encCardInfo;
    private String spare;   //예비공간 47

    public String generateData(){
        //450자리
        String result;
        StringBuilder sb = new StringBuilder();
        sb.append(convertByType(paymentType, 10, RIGHT_SPACE));
        sb.append(convertByType(paymentId, 20, RIGHT_SPACE));
        sb.append(convertByType(cardNum, 20, RIGHT_SPACE));
        sb.append(convertByType(installment, 2, LEFT_ZERO));
        sb.append(convertByType(expiryDate, 4, RIGHT_SPACE));
        sb.append(convertByType(cvcNum, 3, RIGHT_SPACE));
        sb.append(convertByType(amount,10, LEFT_SPACE));
        sb.append(convertByType(vat, 10, LEFT_ZERO));
        sb.append(convertByType(orginPaymentId, 20, RIGHT_SPACE));
        sb.append(convertByType(encCardInfo, 300, RIGHT_SPACE));
        sb.append(convertByType(spare, 47, RIGHT_SPACE));

        dataLength = String.valueOf(sb.length());
        result = convertByType(dataLength, 4, LEFT_SPACE)+sb.toString();

        return result;
    }

    private String convertByType(String source, int length, int padType) {
        StringBuilder builder = new StringBuilder();
        char pch;

        if (source == null) {
            source = "";
        }
        //우측정렬 빈자리 공백
        if (padType == LEFT_SPACE) {
            pch = ' ';
            while(builder.length() < length-source.length()){
                builder.append(pch);
            }
            builder.append(source);
        }
        //우측정렬 빈자리 0
        else if (padType == LEFT_ZERO) {
            pch = '0';
            while(builder.length() < length-source.length()){
                builder.append(pch);
            }
            builder.append(source);
        }
        //좌측정렬 빈자리 공백
        else if (padType == RIGHT_SPACE) {
            pch = ' ';
            builder.append(source);
            while(builder.length() < length) {
                builder.append(pch);
            }
        }
        else
            return null;

        //System.out.println("["+builder.toString()+"] " + builder.length());
        return builder.toString();
    }
}
