package com.contaduria.auth.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.time.LocalDateTime;

/**
 * Entidad para registrar auditoría de accesos y operaciones
 * Mapea directamente a la tabla 'auditoria' existente
 */
@Entity
@Table(name = "auditoria")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idauditoria")
    private Integer idAuditoria;

    @Column(name = "usuarios_idusuarios", nullable = false)
    private Integer usuariosIdUsuarios;

    @Column(name = "tabla")
    private String tabla;

    @Column(name = "proceso")
    private String proceso;

    @Column(name = "fecha")
    private String fecha; // Mantenemos como String para compatibilidad

//    // Campos adicionales para mejor auditoría
//    @Column(name = "detalle")
//    private String detalle;
//
//    @Column(name = "ip_origen")
//    private String ipOrigen;
//
//    @Column(name = "fecha_registro")
//    private LocalDateTime fechaRegistro;

    // Relación con usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarios_idusuarios", insertable = false, updatable = false)
    private Usuario usuario;

    // Constructores
    public Auditoria() {
//        this.fechaRegistro = LocalDateTime.now();
        this.fecha = LocalDateTime.now().toString(); // Para compatibilidad
    }

    public Auditoria(Integer usuariosIdUsuarios, String tabla, String proceso, String detalle) {
        this();
        this.usuariosIdUsuarios = usuariosIdUsuarios;
        this.tabla = tabla;
        this.proceso = proceso;
//        this.detalle = detalle;
    }

    // Getters y Setters
    public Integer getIdAuditoria() { return idAuditoria; }
    public void setIdAuditoria(Integer idAuditoria) { this.idAuditoria = idAuditoria; }

    public Integer getUsuariosIdUsuarios() { return usuariosIdUsuarios; }
    public void setUsuariosIdUsuarios(Integer usuariosIdUsuarios) { this.usuariosIdUsuarios = usuariosIdUsuarios; }

    public String getTabla() { return tabla; }
    public void setTabla(String tabla) { this.tabla = tabla; }

    public String getProceso() { return proceso; }
    public void setProceso(String proceso) { this.proceso = proceso; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

//    public String getDetalle() { return detalle; }
//    public void setDetalle(String detalle) { this.detalle = detalle; }
//
//    public String getIpOrigen() { return ipOrigen; }
//    public void setIpOrigen(String ipOrigen) { this.ipOrigen = ipOrigen; }
//
//    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
//    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
