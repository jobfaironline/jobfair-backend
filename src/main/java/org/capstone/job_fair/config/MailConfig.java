package org.capstone.job_fair.config;

import org.capstone.job_fair.constants.MailServerConstant;
import org.capstone.job_fair.constants.ResetPasswordTokenConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        props.put(MailServerConstant.PROTOCOL_KEY, MailServerConstant.PROTOCOL_VALUE);
        props.put(MailServerConstant.SMTP_AUTHENTICATION, MailServerConstant.TRUE);
        props.put(MailServerConstant.SMTP_START_TLS, MailServerConstant.TRUE);
        props.put(MailServerConstant.MAIL_DEBUG, MailServerConstant.TRUE);

        return mailSender;
    }

}
