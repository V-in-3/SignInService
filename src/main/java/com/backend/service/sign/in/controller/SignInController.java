package com.backend.service.sign.in.controller;

import com.backend.service.sign.in.api.ApiOpCheckOtp;
import com.backend.service.sign.in.api.ApiOpSendOtp;
import com.backend.service.sign.in.dto.EmailRequest;
import com.backend.service.sign.in.dto.EmailResponse;
import com.backend.service.sign.in.dto.OtpRequest;
import com.backend.service.sign.in.service.SignInService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.backend.service.sign.in.Constants.CHECK;
import static com.backend.service.sign.in.Constants.ROUTE_SIGN_IN;

@Slf4j
@Tag(name = "Sing in", description = "Customer verification")
@RestController
@RequestMapping(ROUTE_SIGN_IN)
public class SignInController {

    private final SignInService signInService;

    public SignInController(SignInService signInService) {
        this.signInService = signInService;
    }

    @ApiOpSendOtp
    @PostMapping
    public EmailResponse sendOtp(@Valid @RequestBody EmailRequest request) {
        log.debug("Received EmailRequest: {}", request);
        return signInService.sendOtp(request);
    }

    @ApiOpCheckOtp
    @PostMapping(CHECK)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void checkOtp(@Valid @RequestBody OtpRequest request) {
        log.debug("Received OtpRequest: {}", request);
        signInService.checkOtp(request);
    }
}