package com.contaduria.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Entidad que representa a un usuario del sistema
 * Implementa UserDetails para integración con Spring Security
 * Mapea directamente a la tabla 'usuarios' existente
 */
@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @Column(name = "idusuarios")
    private Integer idUsuarios;

    @Column(name = "idEmpleados", nullable = false)
    @NotNull(message = "El empleado es requerido")
    private Integer idEmpleados;

    @Column(name = "usuario", nullable = false, unique = true)
    @NotBlank(message = "El nombre de usuario es requerido")
    @Size(max = 45, message = "El usuario no puede exceder 45 caracteres")
    private String usuario;

    @Column(name = "contrasena", nullable = false)
    @NotBlank(message = "La contraseña es requerida")
    @Size(max = 150, message = "La contraseña no puede exceder 150 caracteres")
    private String contrasena;

    @Column(name = "activo", nullable = false)
    private int activo = 1;

//    // Campos adicionales para seguridad (agregar a BD si es necesario)
//    @Column(name = "ultimo_acceso")
//    private LocalDateTime ultimoAcceso;
//
//    @Column(name = "intentos_fallidos")
//    private Integer intentosFallidos = 0;
//
//    @Column(name = "cuenta_bloqueada")
//    private Boolean cuentaBloqueada = false;
//
//    @Column(name = "fecha_bloqueo")
//    private LocalDateTime fechaBloqueo;

    // Relaciones
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idEmpleados", insertable = false, updatable = false)
    private Empleado empleado;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "rolesusuarios",
            joinColumns = @JoinColumn(name = "idUsuarios"),
            inverseJoinColumns = @JoinColumn(name = "idRoles")
    )
    private Set<Rol> roles;

    // Constructores
    public Usuario() {}

    public Usuario(String usuario, String contrasena, Integer idEmpleados) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.idEmpleados = idEmpleados;
        this.activo = 1;
    }

    // Implementación de UserDetails para Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre().toUpperCase()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return usuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

//    @Override
////    public boolean isAccountNonLocked() {
////        return !Boolean.TRUE.equals(cuentaBloqueada);
////    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return activo == 1;
    }

    // Métodos de utilidad
//    public void registrarAccesoExitoso() {
//        this.ultimoAcceso = LocalDateTime.now();
//        this.intentosFallidos = 0;
//        this.cuentaBloqueada = false;
//        this.fechaBloqueo = null;
//    }

//    public void registrarAccesoFallido() {
//        this.intentosFallidos = (this.intentosFallidos != null ? this.intentosFallidos : 0) + 1;
//
//        // Bloquear cuenta después de 5 intentos fallidos
//        if (this.intentosFallidos >= 5) {
//            this.cuentaBloqueada = true;
//            this.fechaBloqueo = LocalDateTime.now();
//        }
//    }

//    public boolean puedeAcceder() {
//        return Boolean.TRUE.equals(activo) && !Boolean.TRUE.equals(cuentaBloqueada);
//    }

    public String getNombreCompleto() {
        if (empleado != null && empleado.getPersona() != null) {
            return empleado.getPersona().getNombreCompleto();
        }
        return usuario;
    }

    public String getEmail() {
        if (empleado != null && empleado.getPersona() != null) {
            return empleado.getPersona().getCorreo();
        }
        return null;
    }

    // Getters y Setters
    public Integer getIdUsuarios() { return idUsuarios; }
    public void setIdUsuarios(Integer idUsuarios) { this.idUsuarios = idUsuarios; }

    public Integer getIdEmpleados() { return idEmpleados; }
    public void setIdEmpleados(Integer idEmpleados) { this.idEmpleados = idEmpleados; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public int getActivo() { return activo; }
    public void setActivo(int activo) { this.activo = activo; }

//    public LocalDateTime getUltimoAcceso() { return ultimoAcceso; }
//    public void setUltimoAcceso(LocalDateTime ultimoAcceso) { this.ultimoAcceso = ultimoAcceso; }
//
//    public Integer getIntentosFallidos() { return intentosFallidos; }
//    public void setIntentosFallidos(Integer intentosFallidos) { this.intentosFallidos = intentosFallidos; }
//
//    public Boolean getCuentaBloqueada() { return cuentaBloqueada; }
//    public void setCuentaBloqueada(Boolean cuentaBloqueada) { this.cuentaBloqueada = cuentaBloqueada; }
//
//    public LocalDateTime getFechaBloqueo() { return fechaBloqueo; }
//    public void setFechaBloqueo(LocalDateTime fechaBloqueo) { this.fechaBloqueo = fechaBloqueo; }

    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }

    public Set<Rol> getRoles() { return roles; }
    public void setRoles(Set<Rol> roles) { this.roles = roles; }
}
