package com.backend.service.sign.in.service;

public interface OtpSender {

    /**
     * Send otp
     *
     * @param otp must be not null
     */
    void send(String email, String otp);
}
