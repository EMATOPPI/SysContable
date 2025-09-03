package com.contaduria.gateway.config;

import com.contaduria.gateway.filter.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración principal del Gateway
 * Define todas las rutas y sus filtros correspondientes
 */
@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    /**
     * Configura todas las rutas del sistema
     * @param builder Constructor de rutas
     * @return Localizador de rutas configurado
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                // ========================================
                // RUTAS DEL SERVICIO DE AUTENTICACIÓN
                // ========================================

                // Rutas públicas de autenticación (sin filtro JWT)
                .route("auth-publicas", r -> r
                        .path("/api/auth/login", "/api/auth/renovar", "/api/auth/validar", "/api/auth/health")
                        .uri("http://localhost:8082"))

                // Rutas protegidas de autenticación (con filtro JWT)
                .route("auth-protegidas", r -> r
                        .path("/api/auth/**")
                        .filters(f -> f.filter(jwtAuthFilter))
                        .uri("http://localhost:8082"))

                // ========================================
                // RUTAS DEL SERVICIO DE CONTADURÍA
                // ========================================

                // Todas las rutas de contaduría están protegidas
                .route("contaduria-clientes", r -> r
                        .path("/api/clientes/**")
                        .filters(f -> f.filter(jwtAuthFilter))
                        .uri("http://localhost:8083"))

                .route("contaduria-empresas", r -> r
                        .path("/api/empresas/**")
                        .filters(f -> f.filter(jwtAuthFilter))
                        .uri("http://localhost:8083"))

                .route("contaduria-documentos", r -> r
                        .path("/api/documentos/**")
                        .filters(f -> f.filter(jwtAuthFilter))
                        .uri("http://localhost:8083"))

                .route("contaduria-reportes", r -> r
                        .path("/api/reportes/**")
                        .filters(f -> f.filter(jwtAuthFilter))
                        .uri("http://localhost:8083"))

                .route("contaduria-facturas", r -> r
                        .path("/api/facturas/**")
                        .filters(f -> f.filter(jwtAuthFilter))
                        .uri("http://localhost:8083"))

                .route("contaduria-balances", r -> r
                        .path("/api/balances/**")
                        .filters(f -> f.filter(jwtAuthFilter))
                        .uri("http://localhost:8083"))

                // Health check del servicio de contaduría (público)
                .route("contaduria-health", r -> r
                        .path("/api/contaduria/health")
                        .uri("http://localhost:8083"))

                // ========================================
                // RUTAS FUTURAS (PREPARADAS)
                // ========================================

                // Servicio de archivos (futuro)
                /*.route("archivos-service", r -> r
                        .path("/api/archivos/**")
                        .filters(f -> f.filter(jwtAuthFilter))
                        .uri("http://localhost:8084"))*/

                // Servicio de notificaciones (futuro)
                /*.route("notificaciones-service", r -> r
                        .path("/api/notificaciones/**")
                        .filters(f -> f.filter(jwtAuthFilter))
                        .uri("http://localhost:8085"))*/

                .build();
    }
}