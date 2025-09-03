package com.contaduria.auth.repository;

import com.contaduria.auth.entity.Auditoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para manejo de registros de auditoría
 */
@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Integer> {

    /**
     * Busca registros de auditoría por usuario
     * @param usuarioId ID del usuario
     * @param pageable Información de paginación
     * @return Página de registros de auditoría
     */
    Page<Auditoria> findByUsuariosIdUsuarios(Integer usuarioId, Pageable pageable);

    /**
     * Busca registros por proceso específico
     * @param proceso Tipo de proceso
     * @return Lista de registros del proceso
     */
    List<Auditoria> findByProceso(String proceso);

    /**
     * Busca registros por tabla afectada
     * @param tabla Nombre de la tabla
     * @return Lista de registros de la tabla
     */
    List<Auditoria> findByTabla(String tabla);

    /**
     * Busca registros en un rango de fechas
     * @param fechaInicio Fecha inicio
     * @param fechaFin Fecha fin
     * @return Lista de registros en el período
     */
//    @Query("SELECT a FROM Auditoria a WHERE a.fechaRegistro BETWEEN :fechaInicio AND :fechaFin ORDER BY a.fechaRegistro DESC")
//    List<Auditoria> findByFechaRegistroBetween(@Param("fechaInicio") LocalDateTime fechaInicio,
//                                               @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Busca registros de login exitosos
     * @return Lista de logins exitosos
     */
//    @Query("SELECT a FROM Auditoria a WHERE a.proceso = 'LOGIN_EXITOSO' ORDER BY a.fechaRegistro DESC")
//    List<Auditoria> findLoginsExitosos();

    /**
     * Busca registros de intentos fallidos
     * @return Lista de intentos fallidos
     */
    @Query("SELECT a FROM Auditoria a WHERE a.proceso = 'LOGIN_FALLIDO' ORDER BY a.idAuditoria DESC")
    List<Auditoria> findLoginsFallidos();

    /**
     * Cuenta registros por usuario en un período
     * @param usuarioId ID del usuario
     * @param fechaInicio Fecha inicio
     * @param fechaFin Fecha fin
     * @return Número de registros
     */
//    @Query("SELECT COUNT(a) FROM Auditoria a WHERE a.usuariosIdUsuarios = :usuarioId AND a.fechaRegistro BETWEEN :fechaInicio AND :fechaFin")
//    Integer countByUsuarioAndFechaBetween(@Param("usuarioId") Integer usuarioId,
//                                       @Param("fechaInicio") LocalDateTime fechaInicio,
//                                       @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Busca últimos registros por usuario
     * @param usuarioId ID del usuario
     * @param limite Número máximo de registros
     * @return Lista de últimos registros
     */
//    @Query(value = "SELECT a FROM Auditoria a WHERE a.usuariosIdUsuarios = :usuarioId ORDER BY a.fechaRegistro DESC")
//    List<Auditoria> findUltimosRegistrosPorUsuario(@Param("usuarioId") Integer usuarioId, Pageable pageable);
}
