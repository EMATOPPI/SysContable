package com.contaduria.auth.config;

import com.contaduria.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad para el servicio de autenticación
 * Define las reglas de acceso y los beans de seguridad necesarios
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserService userService;

    /**
     * Configura la cadena de filtros de seguridad
     * Define qué endpoints son públicos y cuáles requieren autenticación
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Deshabilitamos CSRF para APIs REST
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sin sesiones
                .authorizeHttpRequests(authz -> authz
                        // Endpoints públicos - no requieren autenticación
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/renovar",
                                "/api/auth/validar",
                                "/api/auth/health",
                                "/api/auth/login-debug"  // AGREGADO TEMPORALMENTE
                        ).permitAll()

                        // ENDPOINTS DE DEBUG - TEMPORALES (ELIMINAR EN PRODUCCIÓN)
                        .requestMatchers("/api/debug/**").permitAll()

                        // Endpoints de administración - requieren rol ADMIN
                        .requestMatchers("/api/auth/admin/**").hasRole("ADMIN")

                        // Actuator para monitoreo
                        .requestMatchers("/actuator/**").permitAll()

                        // Todos los demás endpoints requieren autenticación
                        .anyRequest().authenticated()
                )
                // Configurar el proveedor de autenticación personalizado
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    /**
     * Encoder de contraseñas usando BCrypt
     * BCrypt es más seguro que MD5 o SHA porque incluye salt automático
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // 12 rounds es un balance entre seguridad y performance
    }

    /**
     * Proveedor de autenticación que usa nuestro UserService personalizado
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Manager de autenticación para uso en servicios
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}