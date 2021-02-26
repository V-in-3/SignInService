package com.backend.service.sign.in.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Component
@Validated
@ConfigurationProperties("application.email")
public class EmailProps {

    /**
     * Api key which needs for authentication during calling send-grid library
     */
    @NotBlank
    private String apiKey;

    /**
     * Subject of email
     */
    @NotBlank
    private String subject;

    /**
     * Data of sender
     */
    private UserData from = new UserData();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserData {
        /**
         * User's email
         */
        @NotBlank
        private String email;

        /**
         * User's name
         */
        @NotBlank
        private String name;
    }
}
