package com.backend.service.sign.in.service;

public interface OtpGenerator {

    /**
     * Generation otp code of N digits. Where N = length
     * @param length must be not null
     * @return otp
     */
    String generate(int length);
}