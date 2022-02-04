package org.capstone.job_fair.services.interfaces.util;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;

public interface MailService {
//     CompletableFuture<Void> sendMail(String to, String subject, String body);

     CompletableFuture<Void> sendMail(String recipient, String subject, String content) throws UnsupportedEncodingException, MessagingException;
}
