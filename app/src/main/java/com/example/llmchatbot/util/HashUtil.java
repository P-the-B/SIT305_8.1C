package com.example.llmchatbot.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    // salt is derived from the email so the same password hashes differently per user
    // this prevents rainbow table attacks on the local DB
    public static String sha256(String input) {
        return hash(input);
    }

    public static String sha256WithSalt(String password, String email) {
        // email as salt — deterministic so login always reproduces the same hash
        return hash(email.toLowerCase().trim() + ":" + password);
    }

    private static String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
}