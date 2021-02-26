package com.backend.service.sign.in;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;
import java.util.TimeZone;

@Slf4j
@SpringBootApplication
public class SignInServiceApplication implements ApplicationRunner {

	@Value("${spring.application.name}")
	private String appName;

	public static void main(String[] args) {
		init();
		SpringApplication.run(SignInServiceApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
		if (appName == null) {
			throw new IllegalStateException("'spring.application.name' must not be empty!");
		}
		log.info("{} started", appName);
	}

	private static void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		Locale.setDefault(Locale.US);
	}
}