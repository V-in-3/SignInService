package com.backend.service.sign.in.config;

import com.backend.service.sign.in.web.filter.MandatoryHeaderFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import static com.backend.service.sign.in.Constants.ROUTE_SIGN_IN;

@Slf4j
@Configuration
@AutoConfigureAfter(JacksonAutoConfiguration.class)
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MandatoryHeaderFilter> mandatoryHeaderFilter(ObjectMapper objectMapper) {
        log.debug("Registering MandatoryHeaderFilter");

        var bean = new FilterRegistrationBean<>(new MandatoryHeaderFilter(objectMapper));

        bean.addUrlPatterns(ROUTE_SIGN_IN + "/*");

        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}