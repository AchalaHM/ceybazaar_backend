package com.CeyBazaar.backend.service;

import com.CeyBazaar.backend.dto.Response;
import com.CeyBazaar.backend.util.Constants;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ByteArrayResource;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public Response<String> sendEmail(String to1, String to2, String subject, String htmlBody) {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAA");
        try {
            System.out.println("BBBBBBBBBBBBBBBBBBBBB");
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            System.out.println("CCCCCCCCCCCCCCCCCCCCCCCC");

            helper.setTo(to1);
            helper.setSubject(subject);
            helper.setFrom("no-reply@ceybazaar.com"); // Use a valid email address
            helper.setText(htmlBody, true); // This enables HTML content
            System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
            mailSender.send(message);
            logger.info("Email sent successfully to " + to1 + " and " + to2);
            System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEE");
            return new Response<>(Constants.SUCCESS, "Email sent successfully",
                    "Email sent successfully to " + to1 + " and " + to2);

        } catch (Exception ex) {
            System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
            logger.error("Error sending email: " + ex.getMessage());
            return new Response<>(Constants.RUNTIME_EXCEPTION, null, null);
        }
    }

    @Override
    public Response<String> sendEmailWithAttachment(String to, String subject, String htmlBody, byte[] attachment, String attachmentName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("no-reply@ceybazaar.com");
            helper.setText(htmlBody, true);
            
            helper.addAttachment(attachmentName, new ByteArrayResource(attachment));

            mailSender.send(message);
            logger.info("Email with attachment sent successfully to " + to);
            return new Response<>(Constants.SUCCESS, "Email sent successfully", "Email sent successfully to " + to);

        } catch (Exception ex) {
            logger.error("Error sending email with attachment: " + ex.getMessage());
            return new Response<>(Constants.RUNTIME_EXCEPTION, null, null);
        }
    }

}
