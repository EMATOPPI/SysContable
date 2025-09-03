package com.contaduria.auth.dto.response;

import java.time.LocalDateTime;

/**
 * DTO para reporte de actividad del sistema
 */
public class ReporteActividadResponse {

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Integer totalRegistros;
    private Integer loginsExitosos;
    private Integer loginsFallidos;
    private Integer cambiosContrasena;
    private Integer operacionesCrud;
    private Double porcentajeExitoLogins;

    // Constructores
    public ReporteActividadResponse() {}

    // Getters y Setters
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }

    public Integer getTotalRegistros() { return totalRegistros; }
    public void setTotalRegistros(Integer totalRegistros) { this.totalRegistros = totalRegistros; }

    public Integer getLoginsExitosos() { return loginsExitosos; }
    public void setLoginsExitosos(Integer loginsExitosos) { this.loginsExitosos = loginsExitosos; }

    public Integer getLoginsFallidos() { return loginsFallidos; }
    public void setLoginsFallidos(Integer loginsFallidos) { this.loginsFallidos = loginsFallidos; }

    public Integer getCambiosContrasena() { return cambiosContrasena; }
    public void setCambiosContrasena(Integer cambiosContrasena) { this.cambiosContrasena = cambiosContrasena; }

    public Integer getOperacionesCrud() { return operacionesCrud; }
    public void setOperacionesCrud(Integer operacionesCrud) { this.operacionesCrud = operacionesCrud; }

    public Double getPorcentajeExitoLogins() { return porcentajeExitoLogins; }
    public void setPorcentajeExitoLogins(Double porcentajeExitoLogins) { this.porcentajeExitoLogins = porcentajeExitoLogins; }
}