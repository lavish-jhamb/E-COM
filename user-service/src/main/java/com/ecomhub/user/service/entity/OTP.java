package com.ecomhub.user.service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class OTP {
    @Id
    private String email;

    private String otp;
    private LocalDateTime expiredAt;
}
