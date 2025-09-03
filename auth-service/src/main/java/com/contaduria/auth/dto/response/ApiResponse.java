package com.contaduria.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

/**
 * DTO genérico para todas las respuestas de la API
 * Proporciona estructura consistente para éxito y errores
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean exito;
    private String mensaje;
    private T datos;
    private String error;
    private String codigoError;
    private LocalDateTime timestamp;

    // Constructores
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    // Métodos estáticos para crear respuestas de éxito
    public static <T> ApiResponse<T> exito(T datos) {
        ApiResponse<T> response = new ApiResponse<>();
        response.exito = true;
        response.datos = datos;
        return response;
    }

    public static <T> ApiResponse<T> exito(T datos, String mensaje) {
        ApiResponse<T> response = new ApiResponse<>();
        response.exito = true;
        response.datos = datos;
        response.mensaje = mensaje;
        return response;
    }

    public static <T> ApiResponse<T> exitoSinDatos(String mensaje) {
        ApiResponse<T> response = new ApiResponse<>();
        response.exito = true;
        response.mensaje = mensaje;
        return response;
    }

    // Métodos estáticos para crear respuestas de error
    public static <T> ApiResponse<T> error(String error) {
        ApiResponse<T> response = new ApiResponse<>();
        response.exito = false;
        response.error = error;
        return response;
    }

    public static <T> ApiResponse<T> error(String error, String codigoError) {
        ApiResponse<T> response = new ApiResponse<>();
        response.exito = false;
        response.error = error;
        response.codigoError = codigoError;
        return response;
    }

    public static <T> ApiResponse<T> errorValidacion(String error) {
        ApiResponse<T> response = new ApiResponse<>();
        response.exito = false;
        response.error = error;
        response.codigoError = "VALIDACION_ERROR";
        return response;
    }

    public static <T> ApiResponse<T> errorAutenticacion(String error) {
        ApiResponse<T> response = new ApiResponse<>();
        response.exito = false;
        response.error = error;
        response.codigoError = "AUTH_ERROR";
        return response;
    }

    public static <T> ApiResponse<T> errorAccesoDenegado(String error) {
        ApiResponse<T> response = new ApiResponse<>();
        response.exito = false;
        response.error = error;
        response.codigoError = "ACCESS_DENIED";
        return response;
    }

    // Getters y Setters
    public boolean isExito() { return exito; }
    public void setExito(boolean exito) { this.exito = exito; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public T getDatos() { return datos; }
    public void setDatos(T datos) { this.datos = datos; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getCodigoError() { return codigoError; }
    public void setCodigoError(String codigoError) { this.codigoError = codigoError; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
