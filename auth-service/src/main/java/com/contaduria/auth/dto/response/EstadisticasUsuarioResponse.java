package com.contaduria.auth.dto.response;

import java.time.LocalDateTime;

/**
 * DTO para estadísticas de uso del usuario
 */
public class EstadisticasUsuarioResponse {

    private Integer totalAccesos;
    private LocalDateTime primerAcceso;
    private LocalDateTime ultimoAcceso;
    private Integer accesosHoy;
    private Integer accesosSemana;
    private Integer intentosFallidos;
    private Boolean cuentaBloqueada;

    // Constructores
    public EstadisticasUsuarioResponse() {}

    // Getters y Setters
    public Integer getTotalAccesos() { return totalAccesos; }
    public void setTotalAccesos(Integer totalAccesos) { this.totalAccesos = totalAccesos; }

    public LocalDateTime getPrimerAcceso() { return primerAcceso; }
    public void setPrimerAcceso(LocalDateTime primerAcceso) { this.primerAcceso = primerAcceso; }

    public LocalDateTime getUltimoAcceso() { return ultimoAcceso; }
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) { this.ultimoAcceso = ultimoAcceso; }

    public Integer getAccesosHoy() { return accesosHoy; }
    public void setAccesosHoy(Integer accesosHoy) { this.accesosHoy = accesosHoy; }

    public Integer getAccesosSemana() { return accesosSemana; }
    public void setAccesosSemana(Integer accesosSemana) { this.accesosSemana = accesosSemana; }

    public Integer getIntentosFallidos() { return intentosFallidos; }
    public void setIntentosFallidos(Integer intentosFallidos) { this.intentosFallidos = intentosFallidos; }

    public Boolean getCuentaBloqueada() { return cuentaBloqueada; }
    public void setCuentaBloqueada(Boolean cuentaBloqueada) { this.cuentaBloqueada = cuentaBloqueada; }
}