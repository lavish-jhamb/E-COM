package com.ecomhub.user.service.job;

import com.ecomhub.user.service.repository.OtpRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class OtpCleanupJob {

    @Autowired
    private OtpRepository otpRepository;

    @Transactional
    @Scheduled(cron = "0 59 23 * * *") // Every day at 23:59
    public void cleanup() {
        LocalDateTime now = LocalDateTime.now();
        otpRepository.deleteAllByExpiredAtBefore(now);
        log.info("OTP cleanup job executed at {}", now);
    }

}
