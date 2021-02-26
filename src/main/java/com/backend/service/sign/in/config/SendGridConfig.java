package com.backend.service.sign.in.config;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendGridConfig {

    @Value("${application.email.api-key}")
    private String apikey;

    @Bean
    public SendGrid sendGrid(){
        return new SendGrid(apikey);
    }
}
