package com.contaduria.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * Entidad que representa los roles del sistema
 * Mapea directamente a la tabla 'roles' existente
 */
@Entity
@Table(name = "roles")
public class Rol {

    @Id
    @Column(name = "idroles")
    private Integer idRoles;

    @Column(name = "nombre", nullable = false)
    @NotBlank(message = "El nombre del rol es requerido")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    private String nombre;

//    @Column(name = "descripcion")
//    @Size(max = 200, message = "La descripción no puede exceder 200 caracteres")
//    private String descripcion;
//
//    @Column(name = "activo")
//    private Boolean activo = true;

    // Relaciones
    @ManyToMany(mappedBy = "roles")
    private Set<Usuario> usuarios;

    @OneToMany(mappedBy = "rol", fetch = FetchType.EAGER)
    private Set<Permiso> permisos;

    // Constructores
    public Rol() {}

    public Rol(String nombre, String descripcion) {
        this.nombre = nombre;
//        this.descripcion = descripcion;
//        this.activo = true;
    }

    // Métodos de utilidad
//    public boolean estaActivo() {
//        return Boolean.TRUE.equals(activo);
//    }

    // Getters y Setters
    public Integer getIdRoles() { return idRoles; }
    public void setIdRoles(Integer idRoles) { this.idRoles = idRoles; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

//    public String getDescripcion() { return descripcion; }
//    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
//
//    public Boolean getActivo() { return activo; }
//    public void setActivo(Boolean activo) { this.activo = activo; }

    public Set<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(Set<Usuario> usuarios) { this.usuarios = usuarios; }

    public Set<Permiso> getPermisos() { return permisos; }
    public void setPermisos(Set<Permiso> permisos) { this.permisos = permisos; }
}