package com.contaduria.auth.service;

import com.contaduria.auth.dto.response.ReporteActividadResponse;
import com.contaduria.auth.entity.Auditoria;
import com.contaduria.auth.repository.AuditoriaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para gestión de auditoría del sistema
 * Registra todas las operaciones importantes en español
 */
@Service
@Transactional
public class AuditoriaService {

    private static final Logger logger = LoggerFactory.getLogger(AuditoriaService.class);

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    // ========================================
    // MÉTODOS DE REGISTRO DE AUDITORÍA
    // ========================================

    /**
     * Registra un acceso exitoso al sistema
     * @param usuarioId ID del usuario
     * @param detalle Detalle del acceso
     * @param nombreUsuario Nombre del usuario para log
     */
    @Async
    public void registrarAccesoExitoso(Integer usuarioId, String detalle, String nombreUsuario) {
        try {
            Auditoria registro = new Auditoria();
            registro.setUsuariosIdUsuarios(usuarioId);
            registro.setTabla("usuarios");
            registro.setProceso("LOGIN_EXITOSO");
//            registro.setDetalle("Usuario: " + nombreUsuario + " - " + detalle);
//            registro.setIpOrigen(obtenerIpCliente());

            auditoriaRepository.save(registro);

            logger.info("AUDITORÍA - Acceso exitoso registrado para usuario: {} (ID: {})", nombreUsuario, usuarioId);
        } catch (Exception e) {
            logger.error("Error al registrar acceso exitoso en auditoría: {}", e.getMessage(), e);
        }
    }

    /**
     * Registra un intento de acceso fallido
     * @param usuarioId ID del usuario (puede ser null si no se encontró)
     * @param detalle Detalle del fallo
     * @param nombreUsuario Nombre del usuario intentado
     */
    @Async
    public void registrarAccesoFallido(Integer usuarioId, String detalle, String nombreUsuario) {
        try {
            Auditoria registro = new Auditoria();
            registro.setUsuariosIdUsuarios(Math.toIntExact(usuarioId != null ? usuarioId : 0L)); // 0 para usuarios no encontrados
            registro.setTabla("usuarios");
            registro.setProceso("LOGIN_FALLIDO");
//            registro.setDetalle("Usuario intentado: " + nombreUsuario + " - " + detalle);
//            registro.setIpOrigen(obtenerIpCliente());

            auditoriaRepository.save(registro);

            logger.warn("AUDITORÍA - Acceso fallido registrado para usuario: {} (ID: {})", nombreUsuario, usuarioId);
        } catch (Exception e) {
            logger.error("Error al registrar acceso fallido en auditoría: {}", e.getMessage(), e);
        }
    }

    /**
     * Registra un cambio de contraseña exitoso
     * @param usuarioId ID del usuario
     * @param detalle Detalle del cambio
     */
    @Async
    public void registrarCambioContrasenaExitoso(Integer usuarioId, String detalle) {
        try {
            Auditoria registro = new Auditoria();
            registro.setUsuariosIdUsuarios(usuarioId);
            registro.setTabla("usuarios");
            registro.setProceso("CAMBIO_CONTRASENA_EXITOSO");
//            registro.setDetalle(detalle);
//            registro.setIpOrigen(obtenerIpCliente());

            auditoriaRepository.save(registro);

            logger.info("AUDITORÍA - Cambio de contraseña exitoso registrado para usuario ID: {}", usuarioId);
        } catch (Exception e) {
            logger.error("Error al registrar cambio de contraseña exitoso en auditoría: {}", e.getMessage(), e);
        }
    }

    /**
     * Registra un intento fallido de cambio de contraseña
     * @param usuarioId ID del usuario
     * @param detalle Detalle del fallo
     */
    @Async
    public void registrarCambioContrasenaFallido(Integer usuarioId, String detalle) {
        try {
            Auditoria registro = new Auditoria();
            registro.setUsuariosIdUsuarios(usuarioId);
            registro.setTabla("usuarios");
            registro.setProceso("CAMBIO_CONTRASENA_FALLIDO");
//            registro.setDetalle(detalle);
//            registro.setIpOrigen(obtenerIpCliente());

            auditoriaRepository.save(registro);

            logger.warn("AUDITORÍA - Cambio de contraseña fallido registrado para usuario ID: {}", usuarioId);
        } catch (Exception e) {
            logger.error("Error al registrar cambio de contraseña fallido en auditoría: {}", e.getMessage(), e);
        }
    }

    /**
     * Registra el desbloqueo de una cuenta
     * @param usuarioId ID del usuario
     * @param detalle Detalle del desbloqueo
     */
    @Async
    public void registrarDesbloqueoCuenta(Integer usuarioId, String detalle) {
        try {
            Auditoria registro = new Auditoria();
            registro.setUsuariosIdUsuarios(usuarioId);
            registro.setTabla("usuarios");
            registro.setProceso("DESBLOQUEO_CUENTA");
//            registro.setDetalle(detalle);
//            registro.setIpOrigen(obtenerIpCliente());

            auditoriaRepository.save(registro);

            logger.info("AUDITORÍA - Desbloqueo de cuenta registrado para usuario ID: {}", usuarioId);
        } catch (Exception e) {
            logger.error("Error al registrar desbloqueo de cuenta en auditoría: {}", e.getMessage(), e);
        }
    }

    /**
     * Registra el logout de un usuario
     * @param usuarioId ID del usuario
     * @param detalle Detalle del logout
     */
    @Async
    public void registrarLogout(Integer usuarioId, String detalle) {
        try {
            Auditoria registro = new Auditoria();
            registro.setUsuariosIdUsuarios(usuarioId);
            registro.setTabla("usuarios");
            registro.setProceso("LOGOUT");
//            registro.setDetalle(detalle);
//            registro.setIpOrigen(obtenerIpCliente());

            auditoriaRepository.save(registro);

            logger.info("AUDITORÍA - Logout registrado para usuario ID: {}", usuarioId);
        } catch (Exception e) {
            logger.error("Error al registrar logout en auditoría: {}", e.getMessage(), e);
        }
    }

    /**
     * Registra acceso a funcionalidades específicas
     * @param usuarioId ID del usuario
     * @param funcionalidad Funcionalidad accedida
     * @param detalle Detalle del acceso
     */
    @Async
    public void registrarAccesoFuncionalidad(Integer usuarioId, String funcionalidad, String detalle) {
        try {
            Auditoria registro = new Auditoria();
            registro.setUsuariosIdUsuarios(usuarioId);
            registro.setTabla("sistema");
            registro.setProceso("ACCESO_FUNCIONALIDAD");
//            registro.setDetalle("Funcionalidad: " + funcionalidad + " - " + detalle);
//            registro.setIpOrigen(obtenerIpCliente());

            auditoriaRepository.save(registro);

            logger.debug("AUDITORÍA - Acceso a funcionalidad registrado: {} para usuario ID: {}", funcionalidad, usuarioId);
        } catch (Exception e) {
            logger.error("Error al registrar acceso a funcionalidad en auditoría: {}", e.getMessage(), e);
        }
    }

    /**
     * Registra operaciones CRUD en entidades
     * @param usuarioId ID del usuario
     * @param tabla Tabla afectada
     * @param operacion Tipo de operación (CREATE, UPDATE, DELETE)
     * @param entityId ID de la entidad afectada
     * @param detalle Detalle de la operación
     */
    @Async
    public void registrarOperacionCRUD(Integer usuarioId, String tabla, String operacion, Integer entityId, String detalle) {
        try {
            Auditoria registro = new Auditoria();
            registro.setUsuariosIdUsuarios(usuarioId);
            registro.setTabla(tabla);
            registro.setProceso("CRUD_" + operacion.toUpperCase());
//            registro.setDetalle("ID Entidad: " + entityId + " - " + detalle);
//            registro.setIpOrigen(obtenerIpCliente());

            auditoriaRepository.save(registro);

            logger.info("AUDITORÍA - Operación {} en tabla {} registrada para usuario ID: {}", operacion, tabla, usuarioId);
        } catch (Exception e) {
            logger.error("Error al registrar operación CRUD en auditoría: {}", e.getMessage(), e);
        }
    }

    /**
     * Registra errores del sistema
     * @param usuarioId ID del usuario (puede ser null)
     * @param proceso Proceso donde ocurrió el error
     * @param detalle Detalle del error
     */
    @Async
    public void registrarError(Integer usuarioId, String proceso, String detalle) {
        try {
            Auditoria registro = new Auditoria();
            registro.setUsuariosIdUsuarios(Math.toIntExact(usuarioId != null ? usuarioId : 0L));
            registro.setTabla("sistema");
            registro.setProceso("ERROR_" + proceso.toUpperCase());
//            registro.setDetalle(detalle);
//            registro.setIpOrigen(obtenerIpCliente());

            auditoriaRepository.save(registro);

            logger.error("AUDITORÍA - Error registrado en proceso {}: {}", proceso, detalle);
        } catch (Exception e) {
            logger.error("Error al registrar error en auditoría: {}", e.getMessage(), e);
        }
    }

    // ========================================
    // MÉTODOS DE CONSULTA
    // ========================================

    /**
     * Obtiene el historial de auditoría de un usuario
     * @param usuarioId ID del usuario
     * @param pageable Información de paginación
     * @return Página de registros de auditoría
     */
    @Transactional(readOnly = true)
    public Page<Auditoria> obtenerHistorialUsuario(Integer usuarioId, Pageable pageable) {
        logger.debug("Obteniendo historial de auditoría para usuario ID: {}", usuarioId);
        return auditoriaRepository.findByUsuariosIdUsuarios(Math.toIntExact(usuarioId), pageable);
    }

    /**
     * Obtiene registros de auditoría por proceso
     * @param proceso Tipo de proceso
     * @return Lista de registros
     */
    @Transactional(readOnly = true)
    public List<Auditoria> obtenerRegistrosPorProceso(String proceso) {
        logger.debug("Obteniendo registros de auditoría para proceso: {}", proceso);
        return auditoriaRepository.findByProceso(proceso);
    }

    /**
     * Obtiene registros en un rango de fechas
     * @param fechaInicio Fecha inicio
     * @param fechaFin Fecha fin
     * @return Lista de registros
     */
//    @Transactional(readOnly = true)
//    public List<Auditoria> obtenerRegistrosPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
//        logger.debug("Obteniendo registros de auditoría entre {} y {}", fechaInicio, fechaFin);
//        return auditoriaRepository.findByFechaRegistroBetween(fechaInicio, fechaFin);
//    }

    /**
     * Obtiene estadísticas de acceso de un usuario
     * @param usuarioId ID del usuario
     * @param dias Número de días hacia atrás
     * @return Número de accesos en el período
     */
//    @Transactional(readOnly = true)
//    public Integer obtenerEstadisticasAcceso(Integer usuarioId, int dias) {
//        LocalDateTime fechaInicio = LocalDateTime.now().minusDays(dias);
//        LocalDateTime fechaFin = LocalDateTime.now();
//
//        return auditoriaRepository.countByUsuarioAndFechaBetween(Math.toIntExact(usuarioId), fechaInicio, fechaFin);
//    }

    /**
     * Obtiene los últimos logins exitosos del sistema
     * @return Lista de últimos logins exitosos
     */
//    @Transactional(readOnly = true)
//    public List<Auditoria> obtenerUltimosLoginsExitosos() {
//        return auditoriaRepository.findLoginsExitosos();
//    }

    /**
     * Obtiene los últimos intentos fallidos del sistema
     * @return Lista de últimos intentos fallidos
     */
    @Transactional(readOnly = true)
    public List<Auditoria> obtenerUltimosLoginsFallidos() {
        return auditoriaRepository.findLoginsFallidos();
    }

    /**
     * Genera reporte de actividad del sistema
     * @param fechaInicio Fecha inicio del reporte
     * @param fechaFin Fecha fin del reporte
     * @return Map con estadísticas del período
     */
    @Transactional(readOnly = true)
    public ReporteActividadResponse generarReporteActividad(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        logger.info("Generando reporte de actividad entre {} y {}", fechaInicio, fechaFin);

//        List<Auditoria> registros = auditoriaRepository.findByFechaRegistroBetween(fechaInicio, fechaFin);

        ReporteActividadResponse reporte = new ReporteActividadResponse();
        reporte.setFechaInicio(fechaInicio);
        reporte.setFechaFin(fechaFin);
//        reporte.setTotalRegistros(registros.size());

//        // Contar por tipo de proceso
//        Integer loginsExitosos = Math.toIntExact(registros.stream().filter(r -> "LOGIN_EXITOSO".equals(r.getProceso())).count());
//        Integer loginsFallidos = Math.toIntExact(registros.stream().filter(r -> "LOGIN_FALLIDO".equals(r.getProceso())).count());
//        Integer cambiosContrasena = Math.toIntExact(registros.stream().filter(r -> r.getProceso().contains("CAMBIO_CONTRASENA")).count());
//        Integer operacionesCrud = Math.toIntExact(registros.stream().filter(r -> r.getProceso().startsWith("CRUD_")).count());

//        reporte.setLoginsExitosos(loginsExitosos);
//        reporte.setLoginsFallidos(loginsFallidos);
//        reporte.setCambiosContrasena(cambiosContrasena);
//        reporte.setOperacionesCrud(operacionesCrud);
//
//        // Calcular porcentaje de éxito
//        if (loginsExitosos + loginsFallidos > 0) {
//            double porcentajeExito = (double) loginsExitosos / (loginsExitosos + loginsFallidos) * 100;
//            reporte.setPorcentajeExitoLogins(porcentajeExito);
//        }

        return reporte;
    }

    // ========================================
    // MÉTODOS PRIVADOS DE UTILIDAD
    // ========================================

    /**
     * Obtiene la IP del cliente actual
     * @return IP del cliente o "unknown" si no se puede determinar
     */
    private String obtenerIpCliente() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();

                // Verificar headers comunes de proxy
                String ip = request.getHeader("X-Forwarded-For");
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("Proxy-Client-IP");
                }
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("WL-Proxy-Client-IP");
                }
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_CLIENT_IP");
                }
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                }
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }

                // Si hay múltiples IPs, tomar la primera
                if (ip != null && ip.contains(",")) {
                    ip = ip.split(",")[0].trim();
                }

                return ip;
            }
        } catch (Exception e) {
            logger.debug("No se pudo obtener la IP del cliente: {}", e.getMessage());
        }

        return "unknown";
    }
}
