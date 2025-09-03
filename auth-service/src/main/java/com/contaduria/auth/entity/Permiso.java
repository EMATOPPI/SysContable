package com.contaduria.auth.entity;

import jakarta.persistence.*;

/**
 * Entidad que representa los permisos de acceso a menús
 * Mapea directamente a la tabla 'permisos' existente
 */
@Entity
@Table(name = "permisos")
@IdClass(PermisoId.class)
public class Permiso {

    @Id
    @Column(name = "idroles")
    private Integer idRoles;

    @Id
    @Column(name = "idmenus")
    private Integer idMenus;

    @Column(name = "ver")
    private Integer ver;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idroles", insertable = false, updatable = false)
    private Rol rol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idmenus", insertable = false, updatable = false)
    private Menu menu;

    // Constructores
    public Permiso() {}

    public Permiso(Integer idRoles, Integer idMenus, Integer ver) {
        this.idRoles = idRoles;
        this.idMenus = idMenus;
        this.ver = ver;
    }

    // Métodos de utilidad
    public boolean puedeVer() {
        return ver != null && ver == 1;
    }

    // Getters y Setters
    public Integer getIdRoles() { return idRoles; }
    public void setIdRoles(Integer idRoles) { this.idRoles = idRoles; }

    public Integer getIdMenus() { return idMenus; }
    public void setIdMenus(Integer idMenus) { this.idMenus = idMenus; }

    public Integer getVer() { return ver; }
    public void setVer(Integer ver) { this.ver = ver; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public Menu getMenu() { return menu; }
    public void setMenu(Menu menu) { this.menu = menu; }
}
