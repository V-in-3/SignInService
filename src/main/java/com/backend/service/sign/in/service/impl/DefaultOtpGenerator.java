package com.backend.service.sign.in.service.impl;

import com.backend.service.sign.in.service.OtpGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
public class DefaultOtpGenerator implements OtpGenerator {

    @Override
    public String generate(int length) {
        var current = ThreadLocalRandom.current();
        StringBuilder res = new StringBuilder(length);
        res.append(current.nextInt(1, 10));
        for (int i = 1; i < length; i++) {
            res.append(current.nextInt(0, 10));
        }
        log.debug("Otp generated {}", res);
        return res.toString();
    }
}