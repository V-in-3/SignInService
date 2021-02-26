package com.backend.service.sign.in.service;

import com.backend.service.sign.in.dto.EmailRequest;
import com.backend.service.sign.in.dto.EmailResponse;
import com.backend.service.sign.in.dto.OtpRequest;
import com.backend.service.sign.in.model.SignIn;
import lombok.NonNull;

public interface SignInService {
    /**
     * Send otp using email service client
     *
     * @param request must not be null
     * @return {@link EmailResponse} as DTO of created {@link SignIn}
     */
    EmailResponse sendOtp(@NonNull EmailRequest request);

    /**
     * Check otp - expired or not
     *
     * @param request must not be null
     */
    void checkOtp(@NonNull OtpRequest request);
}
