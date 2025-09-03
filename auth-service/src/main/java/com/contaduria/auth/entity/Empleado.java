package com.contaduria.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

/**
 * Entidad que representa a un empleado del sistema
 * Mapea directamente a la tabla 'empleados' existente
 */
@Entity
@Table(name = "empleados")
public class Empleado {

    @Id
    @Column(name = "idempleados")
    private Integer idEmpleados;

    @Column(name = "personas_idpersonas", nullable = false)
    @NotNull(message = "La persona es requerida")
    private Integer personasIdPersonas;

    @Column(name = "cat_puestos_idpuestos", nullable = false)
    @NotNull(message = "El puesto es requerido")
    private Integer catPuestosIdPuestos;

    @Column(name = "telefono", nullable = false)
    @NotNull(message = "El teléfono es requerido")
    private String telefono;

    @Column(name = "salario", nullable = false)
    @NotNull(message = "El salario es requerido")
    private Integer salario;

    @Column(name = "estado", nullable = false)
    @NotNull(message = "El estado es requerido")
    private Integer estado;

    @Column(name = "fechaDesde", nullable = false)
    @NotNull(message = "La fecha de inicio es requerida")
    private LocalDate fechaDesde;

    @Column(name = "fechaFin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "fechaNac", nullable = false)
    @NotNull(message = "La fecha de nacimiento es requerida")
    private LocalDate fechaNac;

    @Column(name = "calcularIps", nullable = false)
    private int calcularIps = 0;

    @Column(name = "sobreMinimo", nullable = false)
    private Integer sobreMinimo = 0;

    @Column(name = "calcularAguinaldo")
    private int calcularAguinaldo = 0;

    @Column(name = "verTodosClientes")
    private int verTodosClientes = 0;

    // Relaciones
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personas_idpersonas", insertable = false, updatable = false)
    private Persona persona;

    @OneToMany(mappedBy = "empleado", fetch = FetchType.LAZY)
    private List<Usuario> usuarios;

    // Constructores
    public Empleado() {}

    // Métodos de utilidad
    public boolean estaActivo() {
        return estado == 1;
    }

    public boolean puedeVerTodosLosClientes() {
        return verTodosClientes == 1;
    }

    // Getters y Setters
    public Integer getIdEmpleados() { return idEmpleados; }
    public void setIdEmpleados(Integer idEmpleados) { this.idEmpleados = idEmpleados; }

    public Integer getPersonasIdPersonas() { return personasIdPersonas; }
    public void setPersonasIdPersonas(Integer personasIdPersonas) { this.personasIdPersonas = personasIdPersonas; }

    public Integer getCatPuestosIdPuestos() { return catPuestosIdPuestos; }
    public void setCatPuestosIdPuestos(Integer catPuestosIdPuestos) { this.catPuestosIdPuestos = catPuestosIdPuestos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Integer getSalario() { return salario; }
    public void setSalario(Integer salario) { this.salario = salario; }

    public Integer getEstado() { return estado; }
    public void setEstado(Integer estado) { this.estado = estado; }

    public LocalDate getFechaDesde() { return fechaDesde; }
    public void setFechaDesde(LocalDate fechaDesde) { this.fechaDesde = fechaDesde; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public LocalDate getFechaNac() { return fechaNac; }
    public void setFechaNac(LocalDate fechaNac) { this.fechaNac = fechaNac; }

    public int getCalcularIps() { return calcularIps; }
    public void setCalcularIps(int calcularIps) { this.calcularIps = calcularIps; }

    public Integer getSobreMinimo() { return sobreMinimo; }
    public void setSobreMinimo(Integer sobreMinimo) { this.sobreMinimo = sobreMinimo; }

    public int getCalcularAguinaldo() { return calcularAguinaldo; }
    public void setCalcularAguinaldo(int calcularAguinaldo) { this.calcularAguinaldo = calcularAguinaldo; }

    public int getVerTodosClientes() { return verTodosClientes; }
    public void setVerTodosClientes(int verTodosClientes) { this.verTodosClientes = verTodosClientes; }

    public Persona getPersona() { return persona; }
    public void setPersona(Persona persona) { this.persona = persona; }

    public List<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }
}
