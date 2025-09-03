package com.contaduria.auth.exception;

import com.contaduria.auth.dto.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de errores para el sistema de autenticación
 * Centraliza el manejo de excepciones y proporciona respuestas consistentes
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Maneja errores de validación de campos
     * @param ex Excepción de validación
     * @return Respuesta con detalles de los errores de validación
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> manejarErroresValidacion(
            MethodArgumentNotValidException ex) {

        logger.warn("Errores de validación encontrados");

        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String nombreCampo = ((FieldError) error).getField();
            String mensajeError = error.getDefaultMessage();
            errores.put(nombreCampo, mensajeError);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.errorValidacion("Errores de validación: " + errores.toString()));
    }

    /**
     * Maneja errores de credenciales incorrectas
     * @param ex Excepción de credenciales incorrectas
     * @return Respuesta de error de autenticación
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> manejarCredencialesIncorrectas(BadCredentialsException ex) {
        logger.warn("Intento de acceso con credenciales incorrectas: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.errorAutenticacion("Usuario o contraseña incorrectos"));
    }

    /**
     * Maneja errores de cuenta deshabilitada
     * @param ex Excepción de cuenta deshabilitada
     * @return Respuesta de error de autenticación
     */
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse<String>> manejarCuentaDeshabilitada(DisabledException ex) {
        logger.warn("Intento de acceso con cuenta deshabilitada: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.errorAutenticacion("Cuenta desactivada"));
    }

    /**
     * Maneja errores de cuenta bloqueada
     * @param ex Excepción de cuenta bloqueada
     * @return Respuesta de error de autenticación
     */
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ApiResponse<String>> manejarCuentaBloqueada(LockedException ex) {
        logger.warn("Intento de acceso con cuenta bloqueada: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.LOCKED)
                .body(ApiResponse.errorAutenticacion("Cuenta bloqueada por múltiples intentos fallidos"));
    }

    /**
     * Maneja errores de acceso denegado
     * @param ex Excepción de acceso denegado
     * @return Respuesta de error de autorización
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> manejarAccesoDenegado(AccessDeniedException ex) {
        logger.warn("Acceso denegado: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.errorAccesoDenegado("No tiene permisos para realizar esta operación"));
    }

    /**
     * Maneja errores de argumentos ilegales
     * @param ex Excepción de argumento ilegal
     * @return Respuesta de error de validación
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> manejarArgumentoIlegal(IllegalArgumentException ex) {
        logger.warn("Argumento ilegal: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.errorValidacion(ex.getMessage()));
    }

    /**
     * Maneja errores de runtime generales
     * @param ex Excepción de runtime
     * @return Respuesta de error genérico
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> manejarRuntimeException(RuntimeException ex) {
        logger.error("Error de runtime: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Error interno del servidor: " + ex.getMessage()));
    }

    /**
     * Maneja cualquier otra excepción no específicamente manejada
     * @param ex Excepción genérica
     * @return Respuesta de error interno
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> manejarExcepcionGenerica(Exception ex) {
        logger.error("Error inesperado: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Error interno del servidor"));
    }
}