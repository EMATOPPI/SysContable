package com.contaduria.auth.dto.response;

import java.time.LocalDate;

/**
 * DTO para información del empleado en las respuestas
 */
public class EmpleadoResponse {

    private Integer idEmpleados;
    private String telefono;
    private Integer estado;
    private LocalDate fechaDesde;
    private LocalDate fechaNac;
    private int verTodosClientes;
    private PersonaResponse persona;

    // Constructores
    public EmpleadoResponse() {}

    // Getters y Setters
    public Integer getIdEmpleados() { return idEmpleados; }
    public void setIdEmpleados(Integer idEmpleados) { this.idEmpleados = idEmpleados; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Integer getEstado() { return estado; }
    public void setEstado(Integer estado) { this.estado = estado; }

    public LocalDate getFechaDesde() { return fechaDesde; }
    public void setFechaDesde(LocalDate fechaDesde) { this.fechaDesde = fechaDesde; }

    public LocalDate getFechaNac() { return fechaNac; }
    public void setFechaNac(LocalDate fechaNac) { this.fechaNac = fechaNac; }

    public int getVerTodosClientes() { return verTodosClientes; }
    public void setVerTodosClientes(int verTodosClientes) { this.verTodosClientes = verTodosClientes; }

    public PersonaResponse getPersona() { return persona; }
    public void setPersona(PersonaResponse persona) { this.persona = persona; }
}