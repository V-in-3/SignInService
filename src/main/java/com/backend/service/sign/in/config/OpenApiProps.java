package com.backend.service.sign.in.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
@ConfigurationProperties("application.open-api")
public class OpenApiProps {
    /**
     * API title.
     */
    @NotBlank
    private String title = "Test project API";

    /**
     * API version.
     */
    @NotBlank
    private String version = "1.0";

    /**
     * API description.
     */
    @NotBlank
    private String description = "Test project API documentation";
}
