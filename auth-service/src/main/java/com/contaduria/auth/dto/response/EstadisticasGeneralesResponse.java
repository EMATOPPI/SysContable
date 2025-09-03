package com.contaduria.auth.dto.response;

/**
 * DTO para estadísticas generales del sistema
 */
public class EstadisticasGeneralesResponse {

    private Long totalUsuarios;
    private Long usuariosActivos;
    private Long usuariosBloqueados;
    private Long loginsDiaActual;
    private Long loginsSemanaActual;
    private Long intentosFallidosHoy;
    private Long intentosFallidosSemana;
    private Double promedioLoginsdiarios;

    // Constructores
    public EstadisticasGeneralesResponse() {}

    // Getters y Setters
    public Long getTotalUsuarios() { return totalUsuarios; }
    public void setTotalUsuarios(Long totalUsuarios) { this.totalUsuarios = totalUsuarios; }

    public Long getUsuariosActivos() { return usuariosActivos; }
    public void setUsuariosActivos(Long usuariosActivos) { this.usuariosActivos = usuariosActivos; }

    public Long getUsuariosBloqueados() { return usuariosBloqueados; }
    public void setUsuariosBloqueados(Long usuariosBloqueados) { this.usuariosBloqueados = usuariosBloqueados; }

    public Long getLoginsDiaActual() { return loginsDiaActual; }
    public void setLoginsDiaActual(Long loginsDiaActual) { this.loginsDiaActual = loginsDiaActual; }

    public Long getLoginsSemanaActual() { return loginsSemanaActual; }
    public void setLoginsSemanaActual(Long loginsSemanaActual) { this.loginsSemanaActual = loginsSemanaActual; }

    public Long getIntentosFallidosHoy() { return intentosFallidosHoy; }
    public void setIntentosFallidosHoy(Long intentosFallidosHoy) { this.intentosFallidosHoy = intentosFallidosHoy; }

    public Long getIntentosFallidosSemana() { return intentosFallidosSemana; }
    public void setIntentosFallidosSemana(Long intentosFallidosSemana) { this.intentosFallidosSemana = intentosFallidosSemana; }

    public Double getPromedioLoginsdiarios() { return promedioLoginsdiarios; }
    public void setPromedioLoginsdiarios(Double promedioLoginsdiarios) { this.promedioLoginsdiarios = promedioLoginsdiarios; }
}
