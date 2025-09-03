package com.contaduria.auth.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para información del usuario en las respuestas
 * No incluye información sensible como contraseñas
 */
public class UsuarioResponse {

    private Integer idUsuarios;
    private String usuario;
    private String nombreCompleto;
    private String email;
    private int activo;
    private LocalDateTime ultimoAcceso;
    private EmpleadoResponse empleado;
    private List<String> roles;
    private int puedeVerTodosClientes;

    // Constructores
    public UsuarioResponse() {}

    // Getters y Setters
    public Integer getIdUsuarios() { return idUsuarios; }
    public void setIdUsuarios(Integer idUsuarios) { this.idUsuarios = idUsuarios; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getActivo() { return activo; }
    public void setActivo(int activo) { this.activo = activo; }

    public LocalDateTime getUltimoAcceso() { return ultimoAcceso; }
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) { this.ultimoAcceso = ultimoAcceso; }

    public EmpleadoResponse getEmpleado() { return empleado; }
    public void setEmpleado(EmpleadoResponse empleado) { this.empleado = empleado; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    public int getPuedeVerTodosClientes() { return puedeVerTodosClientes; }
    public void setPuedeVerTodosClientes(int puedeVerTodosClientes) { this.puedeVerTodosClientes = puedeVerTodosClientes; }
}
