package com.contaduria.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Entidad que representa a una persona en el sistema
 * Mapea directamente a la tabla 'personas' existente
 */
@Entity
@Table(name = "personas")
public class Persona {

    @Id
    @Column(name = "idpersonas")
    private Integer idPersonas;

    @Column(name = "ciudad")
    @Size(max = 45, message = "La ciudad no puede exceder 45 caracteres")
    private String ciudad;

    @Column(name = "direccion")
    @Size(max = 250, message = "La dirección no puede exceder 250 caracteres")
    private String direccion;

    @Column(name = "departamento")
    @Size(max = 45, message = "El departamento no puede exceder 45 caracteres")
    private String departamento;

    @Column(name = "cel")
    @Size(max = 25, message = "El celular no puede exceder 25 caracteres")
    private String cel;

    @Column(name = "correo")
    @Email(message = "Debe ser un email válido")
    @Size(max = 90, message = "El correo no puede exceder 90 caracteres")
    private String correo;

    @Column(name = "nombre")
    @Size(max = 90, message = "El nombre no puede exceder 90 caracteres")
    private String nombre;

    @Column(name = "apellido")
    @Size(max = 45, message = "El apellido no puede exceder 45 caracteres")
    private String apellido;

    @Column(name = "ci", unique = true)
    private Integer ci;

    @Column(name = "ruc")
    private Integer ruc;

    @Column(name = "dv")
    @Size(max = 1, message = "El dígito verificador debe ser 1 carácter")
    private String dv;

    @Column(name = "telefono")
    @Size(max = 25, message = "El teléfono no puede exceder 25 caracteres")
    private String telefono;

    // Relación con empleados
    @OneToMany(mappedBy = "persona", fetch = FetchType.LAZY)
    private List<Empleado> empleados;

    // Constructores
    public Persona() {}

    public Persona(String nombre, String apellido, String correo, Integer ci) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.ci = ci;
    }

    // Métodos de utilidad
    public String getNombreCompleto() {
        return (nombre != null ? nombre : "") + " " + (apellido != null ? apellido : "");
    }

    // Getters y Setters
    public Integer getIdPersonas() { return idPersonas; }
    public void setIdPersonas(Integer idPersonas) { this.idPersonas = idPersonas; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public String getCel() { return cel; }
    public void setCel(String cel) { this.cel = cel; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public Integer getCi() { return ci; }
    public void setCi(Integer ci) { this.ci = ci; }

    public Integer getRuc() { return ruc; }
    public void setRuc(Integer ruc) { this.ruc = ruc; }

    public String getDv() { return dv; }
    public void setDv(String dv) { this.dv = dv; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public List<Empleado> getEmpleados() { return empleados; }
    public void setEmpleados(List<Empleado> empleados) { this.empleados = empleados; }
}