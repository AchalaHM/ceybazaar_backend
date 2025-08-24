package com.CeyBazaar.backend.service;

import com.CeyBazaar.backend.dto.Response;

public interface EmailService {
    Response<String> sendEmail(String to1, String to2, String subject, String body);
    Response<String> sendEmailWithAttachment(String to, String subject, String body, byte[] attachment, String attachmentName);
}
