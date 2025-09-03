package com.contaduria.auth.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para información completa del perfil del usuario
 */
public class PerfilUsuarioResponse {

    private UsuarioResponse usuario;
    private List<RolResponse> roles;
    private List<MenuResponse> menusPermitidos;
    private LocalDateTime ultimaActividad;
    private EstadisticasUsuarioResponse estadisticas;

    // Constructores
    public PerfilUsuarioResponse() {}

    // Getters y Setters
    public UsuarioResponse getUsuario() { return usuario; }
    public void setUsuario(UsuarioResponse usuario) { this.usuario = usuario; }

    public List<RolResponse> getRoles() { return roles; }
    public void setRoles(List<RolResponse> roles) { this.roles = roles; }

    public List<MenuResponse> getMenusPermitidos() { return menusPermitidos; }
    public void setMenusPermitidos(List<MenuResponse> menusPermitidos) { this.menusPermitidos = menusPermitidos; }

    public LocalDateTime getUltimaActividad() { return ultimaActividad; }
    public void setUltimaActividad(LocalDateTime ultimaActividad) { this.ultimaActividad = ultimaActividad; }

    public EstadisticasUsuarioResponse getEstadisticas() { return estadisticas; }
    public void setEstadisticas(EstadisticasUsuarioResponse estadisticas) { this.estadisticas = estadisticas; }
}
