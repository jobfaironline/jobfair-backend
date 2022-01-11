package org.capstone.job_fair.constants;

public class ResetPasswordTokenConstants {
    public static class MailConstant {
        public static final String PROTOCOL_KEY = "mail.transport.protocol";
        public static final String PROTOCOL_VALUE = "smtp";
        public static final String SMTP_AUTHENTICATION = "mail.smtp.auth";
        public static final String TRUE = "true";
        public static final String FALSE = "false";
        public static final String SMTP_START_TLS= "mail.smtp.starttls.enable";
        public static final String MAIL_DEBUG = "mail.debug";
        
        public static final String TO_EMAIL = "phamcaoson1999@gmail.com";
        public static final String MAIL_SUBJECT = "[Job Fair Online] - Reset your password";
        public static final String MAIL_BODY = "Hi " + TO_EMAIL + " Forgot your password?, Here is the OTP for you to reset you password: ";
    }
}
