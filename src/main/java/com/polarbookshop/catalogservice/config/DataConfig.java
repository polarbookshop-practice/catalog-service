package com.polarbookshop.catalogservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJdbcAuditing
public class DataConfig {
    /**
     * Returns the currently authenticated user for auditing purposes
     * @return
     */
    @Bean
    AuditorAware<String> auditorAware(){
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                        // Extracts the SecurityContext
                        // object for the currently
                        // authenticated user from
                        // SecurityContextHolder
                        .map(SecurityContext::getAuthentication)
                        // Handles the case when the user is no authenticated
                        .filter(Authentication::isAuthenticated)
                        .map(Authentication::getName);

     }
}
