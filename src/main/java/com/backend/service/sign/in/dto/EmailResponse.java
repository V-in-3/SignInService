package com.backend.service.sign.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.util.UUID;

@Schema(
        name = "EmailResponse",
        description = "Returning sign-Id when email was sent successfully"
)
@Value
public class EmailResponse {

    @Schema(description = "Sign Id", required = true)
    UUID id;
}
