package com.backend.service.sign.in.service.impl;

import com.backend.service.sign.in.config.OtpProps;
import com.backend.service.sign.in.dto.EmailRequest;
import com.backend.service.sign.in.dto.EmailResponse;
import com.backend.service.sign.in.dto.OtpRequest;
import com.backend.service.sign.in.model.SignIn;
import com.backend.service.sign.in.repo.SignInRepo;
import com.backend.service.sign.in.service.OtpGenerator;
import com.backend.service.sign.in.service.OtpSender;
import com.backend.service.sign.in.service.SignInService;
import com.backend.service.sign.in.web.exception.ApiException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class DefaultSignInService implements SignInService {

    private final OtpGenerator otpGenerator;
    private final OtpSender otpSender;
    private final OtpProps otpProps;
    private final SignInRepo signInRepo;

    public DefaultSignInService(OtpGenerator otpGenerator, OtpSender otpSender, OtpProps otpProps, SignInRepo signInRepo) {
        this.otpGenerator = otpGenerator;
        this.otpSender = otpSender;
        this.otpProps = otpProps;
        this.signInRepo = signInRepo;
    }

    @Override
    public EmailResponse sendOtp(@NonNull EmailRequest request) {
        String otp = otpGenerator.generate(otpProps.getLength());

        otpSender.send(request.getEmail(), otp);

        SignIn signIn = signInRepo.save(new SignIn(request.getEmail(), otp, Instant.now()));

        return new EmailResponse(signIn.getId());
    }

    @Override
    public void checkOtp(@NonNull OtpRequest request) {
        SignIn signIn = findById(request.getId());

        verifyOtp(signIn, request.getOtp());
    }

    private SignIn findById(UUID id) {
        return signInRepo
                .findById(id)
                .orElseThrow(() -> {
                    log.error("SignIn with Id {} not found", id);
                    return ApiException.notFound("record.not-found");
                });
    }

    private void verifyOtp(SignIn signIn, String otp) {
        if (isOtpExpired(signIn)) {
            log.error("Otp '{}' is expired for SignInId {}", otp, signIn.getId());
            throw ApiException.badRequest("otp.incorrect");
        }
        if (isOtpInCorrect(signIn, otp)) {
            log.error("Otp '{}' is incorrect SignInId {}", otp, signIn.getId());
            throw ApiException.badRequest("otp.incorrect");
        }
    }

    private boolean isOtpExpired(SignIn sign) {
        return Instant.now().isAfter(sign.getOtpSentAt().plus(otpProps.getTtl()));
    }

    private boolean isOtpInCorrect(SignIn sign, String otp) {
        return !sign.getOtp().equals(otp);
    }
}