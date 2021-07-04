package com.payment.demopaymentrestapi.common;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SeedCrypto {

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final String key = "1234567890123456";
    private static final String iv = "1234567890123456";

    private static final byte keyByte[] = key.getBytes();
    private static final byte ivByte[] = iv.getBytes();

    public static String encrypt(String str){
        byte[] enc = null;
        enc = KISA_SEED_CBC.SEED_CBC_Encrypt(keyByte,ivByte,str.getBytes(CHARSET), 0, str.getBytes(CHARSET).length);

        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encArray = encoder.encode(enc);

        return new String(encArray, CHARSET);
    }

    public static String decrypt(String str){
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] enc = decoder.decode(str);

        byte[] dec = KISA_SEED_CBC.SEED_CBC_Decrypt(keyByte,ivByte,enc,0,enc.length);

        return new String(dec, CHARSET);
    }
}
