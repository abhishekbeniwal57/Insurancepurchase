package com.insurance.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic().and() // Enable HTTP Basic Authentication
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/insurances").permitAll()
                        .requestMatchers("/api/users/register").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        // Protected endpoints
                        .requestMatchers("/api/insurances/recommended").authenticated()
                        .requestMatchers("/api/insurances/**").permitAll()
                        .requestMatchers("/api/purchases/**").authenticated()
                        .requestMatchers("/api/users/**").authenticated()
                        .anyRequest().authenticated());

        // Allow H2 console frame options
        http.headers(headers -> headers.frameOptions().disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}