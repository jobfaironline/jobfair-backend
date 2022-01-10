package org.capstone.job_fair.services;

public interface MailService {
     void sendMail(String to, String subject, String body);
     void sendPreConfiguredMail(String message);
}
