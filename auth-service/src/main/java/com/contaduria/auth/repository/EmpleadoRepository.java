package com.contaduria.auth.repository;

import com.contaduria.auth.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para manejo de datos de empleados
 */
@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    /**
     * Busca empleados activos (estado = 1)
     * @return Lista de empleados activos
     */
    @Query("SELECT e FROM Empleado e WHERE e.estado = 1")
    List<Empleado> findEmpleadosActivos();

    /**
     * Busca empleado por ID de persona
     * @param personaId ID de la persona
     * @return Empleado encontrado o vacío
     */
    Optional<Empleado> findByPersonasIdPersonas(Integer personaId);

    /**
     * Busca empleados con información completa de persona
     * @return Lista de empleados con datos de persona cargados
     */
    @Query("SELECT e FROM Empleado e LEFT JOIN FETCH e.persona p WHERE e.estado = 1")
    List<Empleado> findEmpleadosActivosConPersona();

    /**
     * Busca empleados que pueden ver todos los clientes
     * @return Lista de empleados con permisos especiales
     */
    List<Empleado> findByVerTodosClientesTrue();

    /**
     * Busca empleados por puesto
     * @param puestoId ID del puesto
     * @return Lista de empleados del puesto
     */
    List<Empleado> findByCatPuestosIdPuestos(Integer puestoId);

    /**
     * Busca empleados contratados en un período
     * @param fechaInicio Fecha inicio
     * @param fechaFin Fecha fin
     * @return Lista de empleados contratados en el período
     */
    List<Empleado> findByFechaDesdeBetween(LocalDate fechaInicio, LocalDate fechaFin);
}
