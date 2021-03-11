package com.backend.service.sign.in.api;

import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Operation(summary = "Send otp")
@ApiDefaultRequest
@ApiDefaultResponse
@ApiResponseOk("Otp was sent successfully")
@ApiResponseUnprocessableEntity("In case the validation check failed")
@ApiResponseNotFound("In case the validation check not found")
public @interface ApiOpSendOtp {
}
