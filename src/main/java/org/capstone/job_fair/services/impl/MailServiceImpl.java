package org.capstone.job_fair.services.impl;

import org.capstone.job_fair.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl  implements MailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SimpleMailMessage preConfiguredMessage;

    @Override
    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    @Override
    public void sendPreConfiguredMail(String message) {
    SimpleMailMessage mailMessage = new SimpleMailMessage(preConfiguredMessage);
    mailMessage.setText(message);
    mailSender.send(mailMessage);
    }
}


