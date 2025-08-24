package com.CeyBazaar.backend.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PayHereGenerator {
    public static String generateHash(String merchantId, String orderId, double amount, String currency, String merchantSecret) throws Exception {
        // Format amount to 2 decimal places
        String formattedAmount = String.format("%.2f", amount);

        // Generate MD5 of merchant secret
        String merchantSecretMd5 = getMd5(merchantSecret).toUpperCase();

        // Build the full string to hash
        String baseString = merchantId + orderId + formattedAmount + currency + merchantSecretMd5;

        // Generate final hash
        return getMd5(baseString).toUpperCase();
    }

    public static String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext.toUpperCase();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
