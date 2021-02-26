package com.backend.service.sign.in.api;

import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Operation(summary = "Check otp")
@ApiDefaultRequest
@ApiDefaultResponse
@ApiResponseNoContent("Otp was checked successfully")
@ApiResponseUnprocessableEntity("In case the validation check failed")
public @interface ApiOpCheckOtp {
}
