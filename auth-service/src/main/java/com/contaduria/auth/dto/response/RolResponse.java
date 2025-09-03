package com.contaduria.auth.dto.response;

import java.util.List;

/**
 * DTO para información de roles en las respuestas
 */
public class RolResponse {

    private Integer idRoles;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    private List<MenuResponse> menusPermitidos;

    // Constructores
    public RolResponse() {}

    public RolResponse(Integer idRoles, String nombre, String descripcion) {
        this.idRoles = idRoles;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public Integer getIdRoles() { return idRoles; }
    public void setIdRoles(Integer idRoles) { this.idRoles = idRoles; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public List<MenuResponse> getMenusPermitidos() { return menusPermitidos; }
    public void setMenusPermitidos(List<MenuResponse> menusPermitidos) { this.menusPermitidos = menusPermitidos; }
}
