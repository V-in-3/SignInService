package com.backend.service.sign.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Schema(
        name = "OtpRequest",
        description = "Request for checking otp"
)
@Value
public class OtpRequest {

    @Schema(description = "Sign Id", required = true)
    @NotNull
    UUID id;

    @Schema(description = "Value of otp", required = true)
    @NotBlank(message = "must not be null or empty")
    String otp;
}