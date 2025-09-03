package com.contaduria.auth.repository;

import com.contaduria.auth.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para manejo de menús del sistema
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    /**
     * Busca menús activos ordenados por orden
     * @return Lista de menús activos ordenados
     */
//    List<Menu> findByActivoTrueOrderByOrden();

    /**
     * Busca menú por nombre
     * @param nombre Nombre del menú
     * @return Menú encontrado o vacío
     */
    Optional<Menu> findByNombre(String nombre);

    /**
     * Busca menú por URL
     * @param url URL del menú
     * @return Menú encontrado o vacío
     */
//    Optional<Menu> findByUrl(String url);

    /**
     * Busca menús activos con sus permisos
     * @return Lista de menús con permisos cargados
     */
//    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.permisos p WHERE m.activo = true ORDER BY m.orden")
//    List<Menu> findMenusActivosConPermisos();

    /**
     * Busca menús accesibles por un usuario específico
     * @param usuarioId ID del usuario
     * @return Lista de menús que el usuario puede ver
     */
    @Query("SELECT DISTINCT m FROM Menu m " +
            "JOIN m.permisos p " +
            "JOIN p.rol r " +
            "JOIN r.usuarios u " +
            "WHERE u.idUsuarios = :usuarioId AND p.ver = 1")
    List<Menu> findMenusAccesiblesPorUsuario(@Param("usuarioId") Integer usuarioId);

    /**
     * Cuenta menús activos
     * @return Número total de menús activos
//     */
//    Integer countByActivoTrue();

    /**
     * Verifica si existe un menú con la URL dada
     * @param url URL a verificar
     * @return true si existe, false si no
     */
//    boolean existsByUrl(String url);

    /**
     * Busca menús por orden específico
     * @param orden Orden a buscar
     * @return Lista de menús con ese orden
     */
//    List<Menu> findByOrden(Integer orden);
}