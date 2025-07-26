package com.app.park_api.config;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// class to enable JPA auditing and provide the current auditor
@EnableJpaAuditing
@Configuration
public class SpringJpaAuditConfig implements AuditorAware<String> {

    // will get the authenticated user from the security context and return the username or null
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return Optional.of(authentication.getName());
        }
        return null;
    }
    
}
