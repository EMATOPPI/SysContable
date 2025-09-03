package com.contaduria.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * Entidad básica que representa los menús del sistema
 * Mapea a la tabla 'menus' referenciada en permisos
 */
@Entity
@Table(name = "menus")
public class Menu {

    @Id
    @Column(name = "idmenus")
    private Integer idMenus;

    @Column(name = "nombre")
    @NotBlank(message = "El nombre del menú es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

//    @Column(name = "descripcion")
//    @Size(max = 200, message = "La descripción no puede exceder 200 caracteres")
//    private String descripcion;
//
//    @Column(name = "url")
//    @Size(max = 200, message = "La URL no puede exceder 200 caracteres")
//    private String url;
//
//    @Column(name = "icono")
//    @Size(max = 50, message = "El icono no puede exceder 50 caracteres")
//    private String icono;
//
//    @Column(name = "orden")
//    private Integer orden;
//
//    @Column(name = "activo")
//    private Boolean activo = true;

    // Relaciones
    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
    private Set<Permiso> permisos;

    // Constructores
    public Menu() {}

    public Menu(String nombre, String url) {
        this.nombre = nombre;
//        this.url = url;
//        this.activo = true;
    }

    // Getters y Setters
    public Integer getIdMenus() { return idMenus; }
    public void setIdMenus(Integer idMenus) { this.idMenus = idMenus; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
//
//    public String getDescripcion() { return descripcion; }
//    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
//
//    public String getUrl() { return url; }
//    public void setUrl(String url) { this.url = url; }
//
//    public String getIcono() { return icono; }
//    public void setIcono(String icono) { this.icono = icono; }
//
//    public Integer getOrden() { return orden; }
//    public void setOrden(Integer orden) { this.orden = orden; }
//
//    public Boolean getActivo() { return activo; }
//    public void setActivo(Boolean activo) { this.activo = activo; }

    public Set<Permiso> getPermisos() { return permisos; }
    public void setPermisos(Set<Permiso> permisos) { this.permisos = permisos; }
}