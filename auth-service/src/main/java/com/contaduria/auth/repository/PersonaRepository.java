package com.contaduria.auth.repository;

import com.contaduria.auth.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para manejo de datos de personas
 */
@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {

    /**
     * Busca persona por número de CI
     * @param ci Número de cédula de identidad
     * @return Persona encontrada o vacío
     */
    Optional<Persona> findByCi(Integer ci);

    /**
     * Busca persona por correo electrónico
     * @param correo Email de la persona
     * @return Persona encontrada o vacío
     */
    Optional<Persona> findByCorreo(String correo);

    /**
     * Busca personas por nombre (búsqueda parcial, insensible a mayúsculas)
     * @param nombre Nombre a buscar
     * @return Lista de personas que coinciden
     */
    @Query("SELECT p FROM Persona p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Persona> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);

    /**
     * Busca personas por apellido (búsqueda parcial, insensible a mayúsculas)
     * @param apellido Apellido a buscar
     * @return Lista de personas que coinciden
     */
    @Query("SELECT p FROM Persona p WHERE LOWER(p.apellido) LIKE LOWER(CONCAT('%', :apellido, '%'))")
    List<Persona> findByApellidoContainingIgnoreCase(@Param("apellido") String apellido);

    /**
     * Busca personas por ciudad
     * @param ciudad Ciudad a buscar
     * @return Lista de personas de la ciudad
     */
    List<Persona> findByCiudad(String ciudad);

    /**
     * Busca personas por departamento
     * @param departamento Departamento a buscar
     * @return Lista de personas del departamento
     */
    List<Persona> findByDepartamento(String departamento);

    /**
     * Verifica si existe una persona con el CI dado
     * @param ci Número de cédula
     * @return true si existe, false si no
     */
    boolean existsByCi(Integer ci);

    /**
     * Verifica si existe una persona con el correo dado
     * @param correo Email a verificar
     * @return true si existe, false si no
     */
    boolean existsByCorreo(String correo);

    /**
     * Búsqueda completa por nombre y apellido
     * @param nombre Nombre a buscar
     * @param apellido Apellido a buscar
     * @return Lista de personas que coinciden
     */
    @Query("SELECT p FROM Persona p WHERE " +
            "LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND " +
            "LOWER(p.apellido) LIKE LOWER(CONCAT('%', :apellido, '%'))")
    List<Persona> findByNombreAndApellidoContaining(@Param("nombre") String nombre,
                                                    @Param("apellido") String apellido);
}