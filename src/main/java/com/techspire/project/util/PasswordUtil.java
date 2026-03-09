package com.techspire.project.util;

public class PasswordUtil {

    public static String hashPassword(String password) {
        return Integer.toHexString(password.hashCode());
    }

    public static boolean verifyPassword(String rawPassword, String storedHash) {
        return hashPassword(rawPassword).equals(storedHash);
    }
}