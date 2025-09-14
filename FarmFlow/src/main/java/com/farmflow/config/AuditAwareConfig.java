package com.farmflow.config;

import com.farmflow.config.security.CustomUserDetails;
import com.farmflow.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Slf4j
@Component
public class AuditAwareConfig implements AuditorAware<Integer> {

    @Override
    public Optional<Integer> getCurrentAuditor() {
        log.info("AuditAwareConfig : getCurrentAuditor()");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails) {
            User loggedInUser = ((CustomUserDetails) principal).getUser();
            log.debug("Current auditor: {}", loggedInUser.getId());
            return Optional.of(loggedInUser.getId());
        } else {
            log.debug("No logged-in user, using default auditor: 0");
            return Optional.of(0);
        }
    }
    public Integer getCurrentUserId() {
        Optional<Integer> userIdOpt = getCurrentAuditor();
        return userIdOpt.orElseThrow(() -> new RuntimeException("No authenticated user found"));
    }


}
