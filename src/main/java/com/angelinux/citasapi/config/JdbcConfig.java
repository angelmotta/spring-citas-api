package com.angelinux.citasapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

import java.time.Instant;
import java.util.Optional;

@Configuration
@EnableJdbcAuditing(dateTimeProviderRef = "dateTimeProvider")
public class JdbcConfig extends AbstractJdbcConfiguration {

    @Bean
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(Instant.now());
    }
}
