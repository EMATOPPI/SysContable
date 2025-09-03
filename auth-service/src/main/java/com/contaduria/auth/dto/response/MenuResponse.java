package com.contaduria.auth.dto.response;

/**
 * DTO para información de menús permitidos al usuario
 */
public class MenuResponse {

    private Integer idMenus;
    private String nombre;
    private String descripcion;
    private String url;
    private String icono;
    private Integer orden;
    private Boolean puedeVer;

    // Constructores
    public MenuResponse() {}

    public MenuResponse(Integer idMenus, String nombre, String url, Boolean puedeVer) {
        this.idMenus = idMenus;
        this.nombre = nombre;
        this.url = url;
        this.puedeVer = puedeVer;
    }

    // Getters y Setters
    public Integer getIdMenus() { return idMenus; }
    public void setIdMenus(Integer idMenus) { this.idMenus = idMenus; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getIcono() { return icono; }
    public void setIcono(String icono) { this.icono = icono; }

    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }

    public Boolean getPuedeVer() { return puedeVer; }
    public void setPuedeVer(Boolean puedeVer) { this.puedeVer = puedeVer; }
}