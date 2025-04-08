package com.ecomhub.user.service.job;

import com.ecomhub.user.service.entity.Account;
import com.ecomhub.user.service.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UnverifiedAccountCleanupJob {

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 */2 * *") // runs every 2 days at midnight(00:00)
    public void cleanup() {
        List<Account> accounts = accountRepository.findByVerifiedFalse();
        accountRepository.deleteAll(accounts);
        log.info("Unverified account cleanup job executed. Deleted {} unverified accounts.", accounts.size());
    }

}
