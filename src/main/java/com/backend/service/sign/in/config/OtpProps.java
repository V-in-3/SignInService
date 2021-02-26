package com.backend.service.sign.in.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.Duration;

@Data
@Component
@Validated
@ConfigurationProperties("application.otp")
public class OtpProps {

    /**
     * Length of otp value
     */
    @NotNull
    private Integer length = 4;

    /**
     * TTL of otp (default value: 180s)
     */
    @NotNull
    private Duration ttl = Duration.ofSeconds(180);;
}