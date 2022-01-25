package org.capstone.job_fair.services.impl.util;

import org.capstone.job_fair.services.interfaces.util.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class MailServiceImpl  implements MailService {
    @Autowired
    private JavaMailSender mailSender;

    @Override
    @Async
    public CompletableFuture<Void> sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        System.out.println("Recipient: " + to);
        message.setSubject(subject);
        message.setText(body);
        System.out.println("Begin send email!");
        mailSender.send(message);
        System.out.println("Email sent!");
        return CompletableFuture.completedFuture(null);
    }

}


