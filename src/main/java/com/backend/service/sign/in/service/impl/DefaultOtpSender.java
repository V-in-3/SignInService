package com.backend.service.sign.in.service.impl;

import com.backend.service.sign.in.client.EmailServiceClient;
import com.backend.service.sign.in.config.EmailProps;
import com.backend.service.sign.in.service.OtpSender;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultOtpSender implements OtpSender {

    private static final String TYPE = "text/plain";

    private final EmailServiceClient emailServiceClient;
    private final EmailProps emailProps;

    public DefaultOtpSender(EmailServiceClient emailServiceClient, EmailProps emailProps) {
        this.emailServiceClient = emailServiceClient;
        this.emailProps = emailProps;
    }

    @Override
    public void send(String email, String otp) {
        Email from = new Email(emailProps.getFrom().getEmail(), emailProps.getFrom().getName());
        String subject = emailProps.getSubject();
        Email to = new Email(email);
        Content content = new Content(TYPE, otp);
        Mail mail = new Mail(from, subject, to, content);

        emailServiceClient.sendEmail(mail);
    }
}