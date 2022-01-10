package org.capstone.job_fair.utils;

import java.util.Random;

public class OTPGenerator {
    public static char[] generateOTP() {
        final String OTP;
        final String digits = "0123456789";
        Random random = new Random();
        char[] otp = new char[6];
        for (int i = 0; i < 6; i++) {
            otp[i] = digits.charAt(random.nextInt(digits.length()));
        }
        return otp;
    }
}
