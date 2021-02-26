package com.backend.service.sign.in.client;

import com.sendgrid.helpers.mail.Mail;

public interface EmailServiceClient {

    /**
     * Send email using sendgrid library
     *
     * @param mail must not be null
     */
    void sendEmail(Mail mail);
}