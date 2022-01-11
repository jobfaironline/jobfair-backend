package org.capstone.job_fair.utils;

import java.util.Random;

public class OTPGenerator {
    public static String generateOTP() {
        final String OTP;
        final String digits = "0123456789";
        Random random = new Random();
        StringBuilder str = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            str.append(digits.charAt(random.nextInt(digits.length())));
        }
        return str.toString();
    }
}
