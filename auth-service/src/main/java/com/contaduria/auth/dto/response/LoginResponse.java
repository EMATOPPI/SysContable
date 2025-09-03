package com.contaduria.auth.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para respuesta exitosa de login
 * Contiene toda la información necesaria para el frontend
 */
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private String tipoToken = "Bearer";
    private Integer expiraEn;
    private UsuarioResponse usuario;
    private List<String> roles;
    private List<MenuResponse> menusPermitidos;
    private LocalDateTime fechaLogin;

    // Constructores
    public LoginResponse() {
        this.fechaLogin = LocalDateTime.now();
    }

    public LoginResponse(String accessToken, String refreshToken, Integer expiraEn, UsuarioResponse usuario) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiraEn = expiraEn;
        this.usuario = usuario;
        this.fechaLogin = LocalDateTime.now();
    }

    // Getters y Setters
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public String getTipoToken() { return tipoToken; }
    public void setTipoToken(String tipoToken) { this.tipoToken = tipoToken; }

    public Integer getExpiraEn() { return expiraEn; }
    public void setExpiraEn(Integer expiraEn) { this.expiraEn = expiraEn; }

    public UsuarioResponse getUsuario() { return usuario; }
    public void setUsuario(UsuarioResponse usuario) { this.usuario = usuario; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    public List<MenuResponse> getMenusPermitidos() { return menusPermitidos; }
    public void setMenusPermitidos(List<MenuResponse> menusPermitidos) { this.menusPermitidos = menusPermitidos; }

    public LocalDateTime getFechaLogin() { return fechaLogin; }
    public void setFechaLogin(LocalDateTime fechaLogin) { this.fechaLogin = fechaLogin; }
}
