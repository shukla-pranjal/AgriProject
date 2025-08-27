package com.farmflow.scheduler;


import com.farmflow.entity.User;
import com.farmflow.enums.AccountStatus;
import com.farmflow.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountScheduler {

    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 * * * ?") // every hour
    @Transactional
    public void manageAccounts() {
        LocalDateTime now = LocalDateTime.now();

        // 1️⃣ Disable expired PENDING accounts
        List<User> expiredPendingUsers = userRepository
                .findAllByMetadataStatusAndMetadataVerificationCodeCreatedAtBefore(
                        AccountStatus.PENDING, now.minusHours(24)
                );

        for (User user : expiredPendingUsers) {
            user.getMetadata().setStatus(AccountStatus.DISABLED);
            log.info("Disabled expired PENDING account: {}", user.getEmail());
        }
        userRepository.saveAll(expiredPendingUsers);

        // 2️⃣ Delete long-term DISABLED accounts
        List<User> oldDisabledUsers = userRepository
                .findAllByMetadataStatusAndMetadataVerificationCodeCreatedAtBefore(
                        AccountStatus.DISABLED, now.minusDays(5)
                );

        for (User user : oldDisabledUsers) {
            log.info("Deleting DISABLED account: {}", user.getEmail());
        }
        userRepository.deleteAll(oldDisabledUsers);
    }
}