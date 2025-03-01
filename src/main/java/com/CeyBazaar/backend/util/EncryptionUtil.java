package com.CeyBazaar.backend.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtil {

    private static final String AES = "AES";
    private static final byte[] SECRET_KEY = "1234567890ABCDEF".getBytes();

    public static String encrypt(String data){
        try{
            SecretKey secretKey = new SecretKeySpec(SECRET_KEY ,AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE , secretKey);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex){
            throw new RuntimeException("Error while encrypting data" , ex);
        }
    }
}