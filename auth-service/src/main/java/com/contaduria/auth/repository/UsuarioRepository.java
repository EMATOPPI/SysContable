package com.contaduria.auth.repository;

import com.contaduria.auth.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para manejo de datos de usuarios
 * Incluye consultas personalizadas para autenticación y seguridad
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Busca un usuario por su nombre de usuario
     * @param usuario Nombre de usuario
     * @return Usuario encontrado o vacío
     */
    Optional<Usuario> findByUsuario(String usuario);

    /**
     * Busca un usuario por su nombre de usuario y que esté activo
     * @param usuario Nombre de usuario
     * @return Usuario activo encontrado o vacío
     */
    Optional<Usuario> findByUsuarioAndActivoTrue(String usuario);

    /**
     * Verifica si existe un usuario con el nombre dado
     * @param usuario Nombre de usuario
     * @return true si existe, false si no
     */
    boolean existsByUsuario(String usuario);

    /**
     * Busca usuarios por ID de empleado
     * @param idEmpleados ID del empleado
     * @return Lista de usuarios del empleado
     */
    List<Usuario> findByIdEmpleados(Integer idEmpleados);

    /**
     * Busca usuarios activos
     * @return Lista de usuarios activos
     */
    List<Usuario> findByActivoTrue();

    /**
     * Busca usuarios bloqueados
     * @return Lista de usuarios bloqueados
     */
//    List<Usuario> findByCuentaBloqueadaTrue();

    /**
     * Consulta personalizada para obtener usuario con toda la información necesaria para autenticación
     * Incluye datos del empleado, persona y roles
     * @param usuario Nombre de usuario
     * @return Usuario con toda la información cargada
     */
    @Query("SELECT u FROM Usuario u " +
            "LEFT JOIN FETCH u.empleado e " +
            "LEFT JOIN FETCH e.persona p " +
            "LEFT JOIN FETCH u.roles r " +
            "LEFT JOIN FETCH r.permisos perm " +
            "LEFT JOIN FETCH perm.menu m " +
            "WHERE u.usuario = :usuario AND u.activo = 1")
    Optional<Usuario> findUsuarioCompletoByUsuario(@Param("usuario") String usuario);

    /**
     * Busca usuarios con intentos fallidos mayores a un número específico
     * @param intentos Número de intentos
     * @return Lista de usuarios con muchos intentos fallidos
     */
//    @Query("SELECT u FROM Usuario u WHERE u.intentosFallidos >= :intentos")
//    List<Usuario> findUsuariosConIntentosExcesivos(@Param("intentos") Integer intentos);

    /**
     * Busca usuarios que se han conectado en un rango de fechas
     * @param fechaInicio Fecha inicio
     * @param fechaFin Fecha fin
     * @return Lista de usuarios conectados en el período
     */
//    @Query("SELECT u FROM Usuario u WHERE u.ultimoAcceso BETWEEN :fechaInicio AND :fechaFin")
//    List<Usuario> findUsuariosConectadosEnPeriodo(@Param("fechaInicio") LocalDateTime fechaInicio,
//                                                  @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Cuenta usuarios activos
     * @return Número total de usuarios activos
     */
    Integer countByActivoTrue();

    /**
     * Busca usuarios bloqueados en una fecha específica
     * @param fechaInicio Fecha inicio del bloqueo
     * @param fechaFin Fecha fin del bloqueo
     * @return Lista de usuarios bloqueados en el período
//     */
//    @Query("SELECT u FROM Usuario u WHERE u.cuentaBloqueada = true AND u.fechaBloqueo BETWEEN :fechaInicio AND :fechaFin")
//    List<Usuario> findUsuariosBloqueadosEnPeriodo(@Param("fechaInicio") LocalDateTime fechaInicio,
//                                                  @Param("fechaFin") LocalDateTime fechaFin);
}