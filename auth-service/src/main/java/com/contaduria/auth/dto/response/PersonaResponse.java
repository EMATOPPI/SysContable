package com.contaduria.auth.dto.response;

/**
 * DTO para información de persona en las respuestas
 */
public class PersonaResponse {

    private Integer idPersonas;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String cel;
    private String ciudad;
    private String departamento;
    private Integer ci;

    // Constructores
    public PersonaResponse() {}

    // Método de utilidad
    public String getNombreCompleto() {
        return (nombre != null ? nombre : "") + " " + (apellido != null ? apellido : "");
    }

    // Getters y Setters
    public Integer getIdPersonas() { return idPersonas; }
    public void setIdPersonas(Integer idPersonas) { this.idPersonas = idPersonas; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCel() { return cel; }
    public void setCel(String cel) { this.cel = cel; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public Integer getCi() { return ci; }
    public void setCi(Integer ci) { this.ci = ci; }
}