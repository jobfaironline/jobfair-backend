package org.capstone.job_fair.config;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.ResetPasswordTokenConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Value("${spring.mail.host}")
    private String MAIL_SERVER_HOST;

    @Value("${spring.mail.port}")
    private int MAIL_SERVER_PORT;

    @Value("${spring.mail.username}")
    private String MAIL_SERVER_USERNAME;

    @Value("${spring.mail.password}")
    private String MAIL_SERVER_PASSWORD;


    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(MAIL_SERVER_HOST);
        mailSender.setPort(MAIL_SERVER_PORT);

        mailSender.setUsername(MAIL_SERVER_USERNAME);
        mailSender.setPassword(MAIL_SERVER_PASSWORD);

        Properties props = mailSender.getJavaMailProperties();
        props.put(ResetPasswordTokenConstants.MailConstant.PROTOCOL_KEY, ResetPasswordTokenConstants.MailConstant.PROTOCOL_VALUE);
        props.put(ResetPasswordTokenConstants.MailConstant.SMTP_AUTHENTICATION,ResetPasswordTokenConstants.MailConstant.TRUE);
        props.put(ResetPasswordTokenConstants.MailConstant.SMTP_START_TLS, ResetPasswordTokenConstants.MailConstant.TRUE);
        props.put(ResetPasswordTokenConstants.MailConstant.MAIL_DEBUG, ResetPasswordTokenConstants.MailConstant.TRUE);

        return mailSender;
    }

}
