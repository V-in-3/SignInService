package com.backend.service.sign.in.client;

import com.backend.service.sign.in.client.impl.DefaultEmailServiceClient;
import com.backend.service.sign.in.web.exception.ApiException;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DefaultEmailServiceClient.class)
public class DefaultEmailServiceClientTest {

    @MockBean
    private SendGrid sendGrid;

    @Autowired
    private EmailServiceClient emailServiceClient;

    @Test
    void sendEmailSuccess() throws IOException {
        when(sendGrid.api(any())).thenReturn(new Response(202, null, null));

        emailServiceClient.sendEmail(getMailObject());

        verify(sendGrid).api(any());
    }

    @Test
    void sendEmailErrorFromApi() throws IOException {
        when(sendGrid.api(any())).thenReturn(new Response(400, null, null));

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> emailServiceClient.sendEmail(getMailObject()))
                .withMessageContaining("send.email.fail");

        verify(sendGrid).api(any());
    }

    @Test
    void sendEmailIOException() throws IOException {

        doThrow(new IOException(new RuntimeException("exception"))).when(sendGrid).api(any());

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> emailServiceClient.sendEmail(getMailObject()))
                .withMessageContaining("send.email.fail");

        verify(sendGrid).api(any());
    }

    private Mail getMailObject() {
        Email from = new Email("from@gmail.com", "Name");
        String subject = "Test subject";
        Email to = new Email("to@gmail.com");
        Content content = new Content("text/plain", "123456");
        return new Mail(from, subject, to, content);
    }
}
