package com.example.onbudget;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHashed {
    public static String hashPassword(String password) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Compute the hash of the password
            byte[] hashedPassword = md.digest(password.getBytes());

            // Convert the byte array to a hex string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedPassword) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
