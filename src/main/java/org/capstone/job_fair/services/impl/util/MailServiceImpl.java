package org.capstone.job_fair.services.impl.util;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.services.interfaces.util.MailService;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;

@Service
public class MailServiceImpl  implements MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${from.email.address}")
    private String fromEmailAddress;

    @Override
    @Async
    public CompletableFuture<Void> sendMail(String recipient, String subject, String content) throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromEmailAddress, MessageUtil.getMessage(MessageConstant.Mail.NAME));
        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
        return CompletableFuture.completedFuture(null);
    }

}


