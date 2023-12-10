package com.adiha.EventScheduler.config.securtiy;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * This class is used to configure the security settings for the test profile.
 * It is annotated with @Configuration to indicate that it is a source of bean definitions.
 * The @EnableWebSecurity annotation turns on a number of useful defaults.
 * The @RequiredArgsConstructor generates a constructor with 1 parameter for each field that requires special handling.
 * The @Profile annotation indicates that this configuration should only be used when the 'test' profile is active.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Profile("test")
public class TestSecurityConfig {

    /**
     * This method is used to configure the HttpSecurity.
     * It is annotated with @Bean to indicate that a bean should be produced.
     * It allows all requests and disables CSRF protection.
     *
     * @param httpSecurity the HttpSecurity to modify
     * @return the SecurityFilterChain that the HttpSecurity was built into
     * @throws Exception if an error occurs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests((requests) -> requests
                        .anyRequest()
                        .permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }
}