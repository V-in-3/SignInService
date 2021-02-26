package com.backend.service.sign.in.service;

import com.backend.service.sign.in.config.OtpProps;
import com.backend.service.sign.in.dto.EmailRequest;
import com.backend.service.sign.in.dto.EmailResponse;
import com.backend.service.sign.in.dto.OtpRequest;
import com.backend.service.sign.in.model.SignIn;
import com.backend.service.sign.in.repo.SignInRepo;
import com.backend.service.sign.in.service.impl.DefaultSignInService;
import com.backend.service.sign.in.web.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DefaultSignInService.class, OtpProps.class})
public class DefaultSignInServiceTest {

    public static final String OTP_VALUE = "123456";

    @MockBean
    private OtpGenerator otpGenerator;

    @MockBean
    private OtpSender otpSender;

    @MockBean
    private SignInRepo signInRepo;

    @Autowired
    private OtpProps otpProps;

    @Autowired
    private DefaultSignInService signInService;

    @Test
    void sendOtpSuccess() {
        SignIn sign = new SignIn();
        sign.setId(UUID.randomUUID());

        when(otpGenerator.generate(anyInt())).thenReturn(OTP_VALUE);
        doNothing().when(otpSender).send(any(), any());
        when(signInRepo.save(any())).thenReturn(sign);

        EmailResponse emailResponse = signInService.sendOtp(new EmailRequest("email@ukr.net"));

        assertNotNull(emailResponse);
        assertEquals(emailResponse.getId(), sign.getId());

        InOrder inOrder = inOrder(otpGenerator, otpSender, signInRepo);
        inOrder.verify(otpGenerator).generate(anyInt());
        inOrder.verify(otpSender).send(any(), any());
        inOrder.verify(signInRepo).save(any());
    }

    @Test
    void sendOtpGeneratorError() {
        doThrow(new RuntimeException("Exception")).when(otpGenerator).generate(anyInt());

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> signInService.sendOtp(new EmailRequest("email@ukr.net")))
                .withMessageContaining("Exception");

        verify(otpSender, never()).send(any(),any());
        verify(signInRepo, never()).save(any());

    }

    @Test
    void sendOtpSenderError() {
        when(otpGenerator.generate(anyInt())).thenReturn(OTP_VALUE);
        doThrow(ApiException.internalServerError("send.email.fail")).when(otpSender).send(any(), any());

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> signInService.sendOtp(new EmailRequest("email@ukr.net")))
                .withMessageContaining("send.email.fail");

        verify(otpGenerator).generate(anyInt());
        verify(signInRepo, never()).save(any());
    }

    @Test
    void sendOtpRepoError() {
        when(otpGenerator.generate(anyInt())).thenReturn(OTP_VALUE);
        doNothing().when(otpSender).send(any(), any());
        doThrow(new RuntimeException("could not execute batch")).when(signInRepo).save(any());

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> signInRepo.save(new SignIn()))
                .withMessageContaining("could not execute batch");
    }

    @Test
    void checkOtpSuccess(){
        SignIn sign = new SignIn("email@ukr.net", "123456", Instant.now());
        sign.setId(UUID.randomUUID());

        when(signInRepo.findById(any())).thenReturn(Optional.of(sign));

        signInService.checkOtp(new OtpRequest(UUID.randomUUID(), "123456"));
    }

    @Test
    void checkOtpRecordNotFoundInDb(){
        when(signInRepo.findById(any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> signInService.checkOtp(new OtpRequest(UUID.randomUUID(), "123456")))
                .withMessageContaining("record.not-found");
    }

    @Test
    void checkOtpIsExpired(){
        this.otpProps.setTtl(Duration.ofSeconds(1));

        SignIn sign = new SignIn("email@ukr.net", "123456", Instant.now().minusSeconds(5));
        sign.setId(UUID.randomUUID());

        when(signInRepo.findById(any())).thenReturn(Optional.of(sign));

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> signInService.checkOtp(new OtpRequest(UUID.randomUUID(), "123456")))
                .withMessageContaining("otp.incorrect");
    }

    @Test
    void checkOtpIsIncorrect(){
        SignIn sign = new SignIn("email@ukr.net", "123456", Instant.now());
        sign.setId(UUID.randomUUID());

        when(signInRepo.findById(any())).thenReturn(Optional.of(sign));

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> signInService.checkOtp(new OtpRequest(UUID.randomUUID(), "8765")))
                .withMessageContaining("otp.incorrect");
    }
}