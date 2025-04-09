package com.ecomhub.user.service.service.authentication;

import com.ecomhub.user.service.entity.OTP;
import com.ecomhub.user.service.exception.InvalidOtpException;
import com.ecomhub.user.service.exception.OtpExpiredException;
import com.ecomhub.user.service.exception.OtpNotFoundException;
import com.ecomhub.user.service.repository.OtpRepository;
import com.ecomhub.user.service.service.authentication.smtp.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class OtpService {

    private static final int OTP_MIN_VALUE = 100000;
    private static final int OTP_MAX_VALUE = 999999;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailService emailService;

    @Value("${otp.expiration.minutes}")
    private int expirationMinutes;


    public void sendOtp(String email) {
        String otp = generateOtp(email);
        try {
            emailService.sendOtpEmail(email, otp);
            log.info("OTP sent to email: {}", email);
        } catch (Exception e) {
            log.error("Failed to send OTP to email: {}", email, e);
            throw new RuntimeException("Failed to send OTP");
        }
    }

    private String generateOtp(String email) {
        String code = String.valueOf((int) (Math.random() * OTP_MAX_VALUE) + OTP_MIN_VALUE);

        OTP otp = new OTP();
        otp.setEmail(email);
        otp.setOtp(code);
        otp.setExpiredAt(LocalDateTime.now().plusMinutes(expirationMinutes));
        otpRepository.save(otp);

        log.info("Generated OTP: {} for email: {}", code, email);
        return code;
    }

    public boolean verifyOtp(String email, String otp) {
        OTP otpEntity = otpRepository.findById(email)
                .orElseThrow(() -> new OtpNotFoundException("OTP not found for email: " + email));

        if (!otpEntity.getOtp().equals(otp)) {
            log.warn("Invalid OTP for email: {}", email);
            throw new InvalidOtpException("Invalid OTP");
        }

        if (otpEntity.getExpiredAt().isBefore(LocalDateTime.now())) {
            log.warn("OTP expired for email: {}", email);
            throw new OtpExpiredException("OTP expired");
        }

        return true;
    }

}
