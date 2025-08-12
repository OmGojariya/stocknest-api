package com.stocknest.stocknest_api.service;

public interface MailService {
    void sendEmail(String to, String subject, String body);
}
