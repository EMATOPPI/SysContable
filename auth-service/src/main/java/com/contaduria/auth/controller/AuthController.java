package com.contaduria.auth.controller;

import com.contaduria.auth.dto.request.LoginRequest;
import com.contaduria.auth.dto.request.CambioContrasenaRequest;
import com.contaduria.auth.dto.request.RefreshTokenRequest;
import com.contaduria.auth.dto.response.*;
import com.contaduria.auth.service.AuthService;
import com.contaduria.auth.service.JwtService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador principal para operaciones de autenticación
 * Maneja login, logout, validación de tokens y gestión de sesiones
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // En producción, especificar dominio exacto
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    /**
     * Endpoint para autenticar usuarios
     * @param request Datos de login (usuario y contraseña)
     * @return Respuesta con tokens y información del usuario
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        logger.info("=== INTENTO DE LOGIN ===");
        logger.info("Usuario: {}", request.getUsuario());
        logger.info("Hora: {}", LocalDateTime.now());

        try {
            // Realizar autenticación
            LoginResponse loginResponse = authService.login(request);

            logger.info("=== LOGIN EXITOSO ===");
            logger.info("Usuario: {} autenticado correctamente", request.getUsuario());
            logger.info("Roles: {}", loginResponse.getRoles());

            return ResponseEntity.ok(
                    ApiResponse.exito(loginResponse, "Autenticación exitosa")
            );

        } catch (Exception e) {
            logger.error("=== LOGIN FALLIDO ===");
            logger.error("Usuario: {}", request.getUsuario());
            logger.error("Error: {}", e.getMessage());

            // Mapear diferentes tipos de errores a respuestas apropiadas
            if (e.getMessage().contains("bloqueada")) {
                return ResponseEntity.status(HttpStatus.LOCKED)
                        .body(ApiResponse.errorAutenticacion("Cuenta bloqueada por múltiples intentos fallidos"));
            } else if (e.getMessage().contains("desactivada")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.errorAutenticacion("Cuenta desactivada"));
            } else if (e.getMessage().contains("encontrado") || e.getMessage().contains("incorrecta")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.errorAutenticacion("Usuario o contraseña incorrectos"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error("Error interno del servidor"));
            }
        }
    }

    /**
     * Endpoint para validar tokens de acceso
     * Usado principalmente por el Gateway y otros microservicios
     * @param token Token de acceso a validar
     * @return Información del usuario si el token es válido
     */
    @PostMapping("/validar")
    public ResponseEntity<ApiResponse<ValidacionTokenResponse>> validarToken(
            @RequestHeader("Authorization") String authHeader) {

        logger.debug("Validando token de acceso");

        try {
            // Extraer token del header Authorization
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.errorAutenticacion("Header de autorización inválido"));
            }

            String token = authHeader.substring(7);
            ValidacionTokenResponse validacion = authService.validarToken(token);

            if (validacion.isValido()) {
                return ResponseEntity.ok(
                        ApiResponse.exito(validacion, "Token válido")
                );
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.errorAutenticacion(validacion.getRazonInvalido()));
            }

        } catch (Exception e) {
            logger.error("Error al validar token: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al validar token"));
        }
    }

    /**
     * Endpoint para renovar tokens usando refresh token
     * @param request Solicitud con refresh token
     * @return Nuevo token de acceso
     */
    @PostMapping("/renovar")
    public ResponseEntity<ApiResponse<LoginResponse>> renovarToken(@Valid @RequestBody RefreshTokenRequest request) {
        logger.debug("Renovando token de acceso");

        try {
            LoginResponse response = authService.renovarToken(request.getRefreshToken());

            return ResponseEntity.ok(
                    ApiResponse.exito(response, "Token renovado exitosamente")
            );

        } catch (Exception e) {
            logger.error("Error al renovar token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.errorAutenticacion("No se pudo renovar el token"));
        }
    }

    /**
     * Endpoint para cambiar contraseña
     * @param request Datos del cambio de contraseña
     * @return Confirmación del cambio
     */
    @PostMapping("/cambiar-contrasena")
    public ResponseEntity<ApiResponse<String>> cambiarContrasena(
            @Valid @RequestBody CambioContrasenaRequest request,
            @RequestHeader("Authorization") String authHeader) {

        logger.info("Solicitud de cambio de contraseña");

        try {
            // Extraer usuario ID del token
            String token = authHeader.substring(7);
            Integer usuarioId = Math.toIntExact(jwtService.extraerUsuarioId(token));

            authService.cambiarContrasena(Math.toIntExact(usuarioId), request);

            return ResponseEntity.ok(
                    ApiResponse.exitoSinDatos("Contraseña cambiada exitosamente")
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.errorValidacion(e.getMessage()));
        } catch (Exception e) {
            logger.error("Error al cambiar contraseña: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al cambiar contraseña"));
        }
    }

    /**
     * Endpoint para obtener perfil del usuario autenticado
     * @return Información completa del perfil
     */
    @GetMapping("/perfil")
    public ResponseEntity<ApiResponse<PerfilUsuarioResponse>> obtenerPerfil(
            @RequestHeader("Authorization") String authHeader) {

        logger.debug("Obteniendo perfil de usuario");

        try {
            // Extraer usuario ID del token
            String token = authHeader.substring(7);
            Integer usuarioId = Math.toIntExact(jwtService.extraerUsuarioId(token));

            PerfilUsuarioResponse perfil = authService.obtenerPerfil(Math.toIntExact(usuarioId));

            return ResponseEntity.ok(
                    ApiResponse.exito(perfil, "Perfil obtenido exitosamente")
            );

        } catch (Exception e) {
            logger.error("Error al obtener perfil: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener perfil"));
        }
    }

    /**
     * Endpoint para logout (invalida tokens - implementación básica)
     * @return Confirmación de logout
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @RequestHeader("Authorization") String authHeader) {

        logger.info("Solicitud de logout");

        try {
            // En una implementación completa, aquí se invalidaría el token
            // Por ahora, solo registramos en auditoría
            String token = authHeader.substring(7);
            Integer usuarioId = Math.toIntExact(jwtService.extraerUsuarioId(token));

            // TODO: Implementar invalidación de tokens (blacklist, base de datos, etc.)

            return ResponseEntity.ok(
                    ApiResponse.exitoSinDatos("Sesión cerrada exitosamente")
            );

        } catch (Exception e) {
            logger.error("Error durante logout: {}", e.getMessage(), e);
            return ResponseEntity.ok(
                    ApiResponse.exitoSinDatos("Sesión cerrada")
            );
        }
    }

    /**
     * Endpoint de health check
     * @return Estado del servicio
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("estado", "ACTIVO");
        health.put("servicio", "auth-service");
        health.put("version", "1.0.0");
        health.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(health);
    }

    /**
     * Endpoint para obtener información del token actual (para debugging)
     * Solo disponible en desarrollo
     */
    @GetMapping("/token-info")
    public ResponseEntity<ApiResponse<Map<String, Object>>> obtenerInfoToken(
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.substring(7);
            Map<String, Object> info = jwtService.extraerInformacionCompleta(token);

            return ResponseEntity.ok(
                    ApiResponse.exito(info, "Información del token")
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.errorAutenticacion("Token inválido"));
        }
    }
}
