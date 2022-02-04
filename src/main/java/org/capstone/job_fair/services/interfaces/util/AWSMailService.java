package org.capstone.job_fair.services.interfaces.util;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;

public interface AWSMailService {
    public void sendMail(String recipient, String subject, String content)  throws UnsupportedEncodingException, MessagingException;
}
