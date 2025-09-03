package com.contaduria.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para manejar los datos de login del usuario
 */
public class LoginRequest {

    @NotBlank(message = "El usuario es requerido")
    @Size(max = 45, message = "El usuario no puede exceder 45 caracteres")
    private String usuario;

    @NotBlank(message = "La contraseña es requerida")
    private String contrasena;

    // Constructores
    public LoginRequest() {}

    public LoginRequest(String usuario, String contrasena) {
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    // Getters y Setters
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "usuario='" + usuario + '\'' +
                ", contrasena='[PROTEGIDA]'" +
                '}';
    }
}
