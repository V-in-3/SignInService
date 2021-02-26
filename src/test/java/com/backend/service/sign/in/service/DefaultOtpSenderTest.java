package com.backend.service.sign.in.service;

import com.backend.service.sign.in.client.EmailServiceClient;
import com.backend.service.sign.in.config.EmailProps;
import com.backend.service.sign.in.service.impl.DefaultOtpSender;
import com.backend.service.sign.in.web.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DefaultOtpSender.class, EmailProps.class})
public class DefaultOtpSenderTest {

    public static final String SUBJECT = "Test subject";
    public static final String FROM_EMAIL = "test@gmail.com";
    public static final String FROM_NAME = "Test name";

    public static final String TO_EMAIL = "test2@gmail.com";
    public static final String OTP = "123456";

    @MockBean
    private EmailServiceClient emailServiceClient;

    @Autowired
    private EmailProps emailProps;

    @Autowired
    private DefaultOtpSender otpSender;

    @BeforeEach
    void setUp() {
        emailProps.setSubject(SUBJECT);
        emailProps.setFrom(new EmailProps.UserData(FROM_EMAIL, FROM_NAME));
    }

    @Test
    void sendSuccess() {
        doNothing().when(emailServiceClient).sendEmail(any());

        otpSender.send(TO_EMAIL, OTP);

        verify(emailServiceClient).sendEmail(any());
    }

    @Test
    void sendException() {
        doThrow(ApiException.internalServerError("send.email.fail")).when(emailServiceClient).sendEmail(any());

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> otpSender.send(TO_EMAIL, OTP))
                .withMessageContaining("send.email.fail");

        verify(emailServiceClient).sendEmail(any());

    }
}