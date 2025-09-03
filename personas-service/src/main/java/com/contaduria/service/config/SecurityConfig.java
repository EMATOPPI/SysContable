package com.contaduria.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad para el servicio de contaduría
 * Como la autenticación ya se maneja en el Gateway,
 * este servicio solo necesita configuración básica
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        // Health check público
                        .requestMatchers("/api/contaduria/health").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        // Todo lo demás permitido - el Gateway ya manejó la autenticación
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
