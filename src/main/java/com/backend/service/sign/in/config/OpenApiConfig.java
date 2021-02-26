package com.backend.service.sign.in.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnClass(OpenAPI.class)
@ConditionalOnProperty(value = "springdoc.api-docs.enabled", matchIfMissing = true)
@EnableConfigurationProperties(OpenApiProps.class)
public class OpenApiConfig {

    private final OpenApiProps props;

    public OpenApiConfig(OpenApiProps props) {
        this.props = props;
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title(props.getTitle())
                .version(props.getVersion())
                .description(props.getDescription())
        );
    }
}