package org.capstone.job_fair.utils;

import java.util.Random;

public class PasswordGenerator {
    public static String generatePassword() {
        final String OTP;
        final String digits = "0123456789QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm!@#$%^&*()_";
        Random random = new Random();
        StringBuilder str = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            str.append(digits.charAt(random.nextInt(digits.length())));
        }
        return str.toString();
    }
}
