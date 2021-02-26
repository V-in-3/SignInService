package com.backend.service.sign.in.service;

import com.backend.service.sign.in.service.impl.DefaultOtpGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultOtpGeneratorTest {

    private final DefaultOtpGenerator otpGenerator = new DefaultOtpGenerator();

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    void generateOtpSuccess(int otpLength) {
        String otp = otpGenerator.generate(otpLength);

        assertThat(otp).isNotNull();
        assertThat(otp.length()).isEqualTo(otpLength);
        assertThat(otp).doesNotStartWith("0");
    }
}