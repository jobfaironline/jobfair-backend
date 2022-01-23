package org.capstone.job_fair.services.interfaces.util;

import java.util.concurrent.CompletableFuture;

public interface MailService {
     CompletableFuture<Void> sendMail(String to, String subject, String body);
}
