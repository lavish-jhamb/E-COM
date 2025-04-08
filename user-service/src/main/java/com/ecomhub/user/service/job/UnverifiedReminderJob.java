package com.ecomhub.user.service.job;

import com.ecomhub.user.service.entity.Account;
import com.ecomhub.user.service.repository.AccountRepository;
import com.ecomhub.user.service.service.smtp.EmailService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UnverifiedReminderJob {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // runs every 24 hours at midnight(00:00)
    public void reminder() {
        List<Account> accounts = accountRepository.findByVerifiedFalse();

        for (Account account : accounts) {
            log.info("Sending reminder email to unverified account: {}", account.getEmail());
            emailService.sendReminderEmail(account.getEmail());
        }

    }
}
