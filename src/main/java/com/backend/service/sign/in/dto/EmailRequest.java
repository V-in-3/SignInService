package com.backend.service.sign.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Schema(
        name = "EmailRequest",
        description = "Request for sending otp using customer's email"
)
@Value
public class EmailRequest {

    @Schema(description = "Phone", required = true)
    @NotBlank(message = "must not be null or empty")
    String email;
}
