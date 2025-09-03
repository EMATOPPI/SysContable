package com.contaduria.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para manejar la renovación de tokens
 */
public class RefreshTokenRequest {

    @NotBlank(message = "El refresh token es requerido")
    private String refreshToken;

    // Constructores
    public RefreshTokenRequest() {}

    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // Getters y Setters
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}