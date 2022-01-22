package org.capstone.job_fair.utils;

import org.capstone.job_fair.constants.AccountConstant;

import java.util.Random;

public class PasswordGenerator {
    public static String generatePassword() {
        final String digits = "0123456789QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm!@#$%^&*()_";
        Random random = new Random();
        StringBuilder str = new StringBuilder(AccountConstant.RANDOM_PASSWORD_LENGTH);
        for (int i = 0; i < AccountConstant.RANDOM_PASSWORD_LENGTH; i++) {
            str.append(digits.charAt(random.nextInt(digits.length())));
        }
        return str.toString();
    }
}
