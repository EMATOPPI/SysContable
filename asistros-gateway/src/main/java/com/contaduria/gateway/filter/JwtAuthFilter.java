package com.contaduria.gateway.filter;

import com.contaduria.gateway.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Filtro JWT para el Gateway
 * Valida tokens y agrega headers con información del usuario
 * para que los microservicios no tengan que validar tokens
 */
@Component
public class JwtAuthFilter implements GatewayFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        logger.debug("=== FILTRO JWT EJECUTÁNDOSE ===");
        logger.debug("Ruta solicitada: {}", path);
        logger.debug("Método: {}", request.getMethod());

        // Obtener header Authorization
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Header de autorización faltante o inválido para ruta: {}", path);
            return onError(exchange, "Header de autorización faltante o inválido", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        logger.debug("Token extraído, validando...");

        try {
            // Validar token
            if (!jwtUtil.esTokenValido(token)) {
                logger.warn("Token inválido para ruta: {}", path);
                return onError(exchange, "Token inválido", HttpStatus.UNAUTHORIZED);
            }

            // Verificar si el token ha expirado
            if (jwtUtil.tokenExpirado(token)) {
                logger.warn("Token expirado para ruta: {}", path);
                return onError(exchange, "Token expirado", HttpStatus.UNAUTHORIZED);
            }

            // Extraer información del usuario del token
            String usuarioId = jwtUtil.extraerUsuarioId(token);
            String empleadoId = jwtUtil.extraerEmpleadoId(token);
            String nombreUsuario = jwtUtil.extraerUsuario(token);
            String nombreCompleto = jwtUtil.extraerNombreCompleto(token);
            String email = jwtUtil.extraerEmail(token);
            List<String> roles = jwtUtil.extraerRoles(token);
            List<String> permisos = jwtUtil.extraerPermisos(token);
            Boolean puedeVerTodosClientes = jwtUtil.extraerPuedeVerTodosClientes(token);

            logger.debug("Token válido para usuario: {} (ID: {})", nombreUsuario, usuarioId);
            logger.debug("Roles: {}", roles);

            // Crear request modificado con headers adicionales para los microservicios
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-Usuario-Id", usuarioId)
                    .header("X-Empleado-Id", empleadoId)
                    .header("X-Usuario-Nombre", nombreUsuario)
                    .header("X-Usuario-Nombre-Completo", nombreCompleto)
                    .header("X-Usuario-Email", email != null ? email : "")
                    .header("X-Usuario-Roles", String.join(",", roles))
                    .header("X-Usuario-Permisos", String.join(",", permisos))
                    .header("X-Puede-Ver-Todos-Clientes", puedeVerTodosClientes != null ? puedeVerTodosClientes.toString() : "false")
                    .build();

            logger.debug("Headers agregados al request para microservicios");

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception e) {
            logger.error("Error al validar token: {}", e.getMessage(), e);
            return onError(exchange, "Error al validar token: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Maneja errores de autenticación
     * @param exchange Intercambio web
     * @param mensaje Mensaje de error
     * @param status Código de estado HTTP
     * @return Mono<Void> con la respuesta de error
     */
    private Mono<Void> onError(ServerWebExchange exchange, String mensaje, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);

        // Agregar headers CORS para que el frontend pueda leer la respuesta
        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.getHeaders().add("Access-Control-Allow-Headers", "Authorization, Content-Type");

        logger.error("Error de autenticación: {} - Status: {}", mensaje, status);

        return response.setComplete();
    }
}
