package com.backend.service.sign.in.client.impl;

import com.backend.service.sign.in.client.EmailServiceClient;
import com.backend.service.sign.in.web.exception.ApiException;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
public class DefaultEmailServiceClient implements EmailServiceClient {

    private final static Method HTTP_METHOD = Method.POST;
    private final static String URL = "mail/send";

    private final SendGrid sendGrid;

    public DefaultEmailServiceClient(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

    @Override
    public void sendEmail(Mail mail) {
        Request request = new Request();
        try {
            request.setMethod(HTTP_METHOD);
            request.setEndpoint(URL);
            request.setBody(mail.build());

            Response response = sendGrid.api(request);
            log.debug("Email was send successfully");

            if (response.getStatusCode() != 202) {
                log.error("Got error({}) from send-grid api", response.getStatusCode());
                throw ApiException.internalServerError("send.email.fail");
            }
        } catch (IOException ex) {
            log.error("Couldn't send email", ex);
            throw ApiException.internalServerError("send.email.fail");
        }
    }
}