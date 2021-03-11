package com.backend.service.sign.in.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity

@Table(name = "sign_ins")
public class SignIn {

    @Id
    private UUID id;

    @Version
    protected Short version;

    @Column(nullable = false, updatable = false)
    private String email;

    @Column(nullable = false, updatable = false)
    private String otp;

    @Column(nullable = false, updatable = false)
    private Instant otpSentAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public SignIn(String email, String otp, Instant otpSentAt) {
        this.email = email;
        this.otp = otp;
        this.otpSentAt = otpSentAt;
    }

    @PrePersist
    private void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }
}