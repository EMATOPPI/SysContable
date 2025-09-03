package com.contaduria.auth.repository;

import com.contaduria.auth.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para manejo de roles del sistema
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

    /**
     * Busca rol por nombre
     * @param nombre Nombre del rol
     * @return Rol encontrado o vacío
     */
    Optional<Rol> findByNombre(String nombre);

    /**
     * Busca roles activos
     * @return Lista de roles activos
     */
//    List<Rol> findByActivoTrue();

    /**
     * Busca rol por nombre y que esté activo
     * @param nombre Nombre del rol
     * @return Rol activo encontrado o vacío
     */
//    Optional<Rol> findByNombreAndActivoTrue(String nombre);

    /**
     * Verifica si existe un rol con el nombre dado
     * @param nombre Nombre del rol
     * @return true si existe, false si no
     */
    boolean existsByNombre(String nombre);

    /**
     * Busca roles con sus permisos cargados
     * @return Lista de roles con permisos
     */
//    @Query("SELECT r FROM Rol r LEFT JOIN FETCH r.permisos p LEFT JOIN FETCH p.menu m WHERE r.activo = true")
//    List<Rol> findRolesActivosConPermisos();

    /**
     * Busca rol específico con permisos cargados
     * @param rolId ID del rol
     * @return Rol con permisos cargados
     */
    @Query("SELECT r FROM Rol r LEFT JOIN FETCH r.permisos p LEFT JOIN FETCH p.menu m WHERE r.idRoles = :rolId")
    Optional<Rol> findRolConPermisos(@Param("rolId") Integer rolId);

    /**
     * Cuenta roles activos
     * @return Número total de roles activos
     */
//    Integer countByActivoTrue();
}
