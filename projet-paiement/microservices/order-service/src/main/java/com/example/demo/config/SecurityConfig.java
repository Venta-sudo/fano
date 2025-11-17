package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatchers(matchers -> matchers.requestMatchers("/**"))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                // Actuator DOIT être avant les autres matchers
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/actuator/prometheus").permitAll()
                .requestMatchers("/health").permitAll()
                // API publics
                .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/orders").permitAll()
                // Tout le reste nécessite auth
                .anyRequest().permitAll() // TEMPORAIRE : permets tout pendant tests
            );
        
        return http.build();
    }
}