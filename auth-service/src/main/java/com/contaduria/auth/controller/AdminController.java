package com.contaduria.auth.controller;

import com.contaduria.auth.dto.response.ApiResponse;
import com.contaduria.auth.dto.response.ReporteActividadResponse;
import com.contaduria.auth.entity.Auditoria;
import com.contaduria.auth.service.AuthService;
import com.contaduria.auth.service.AuditoriaService;
import com.contaduria.auth.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador para funciones administrativas
 * Requiere permisos de administrador
 */
@RestController
@RequestMapping("/api/auth/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private AuditoriaService auditoriaService;

    @Autowired
    private JwtService jwtService;

    /**
     * Desbloquea una cuenta de usuario
     * @param usuarioId ID del usuario a desbloquear
     * @return Confirmación de desbloqueo
     */
    @PostMapping("/desbloquear-usuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> desbloquearUsuario(
            @PathVariable Integer usuarioId,
            @RequestHeader("Authorization") String authHeader) {

        logger.info("Solicitud de desbloqueo para usuario ID: {}", usuarioId);

        try {
            // Verificar permisos del usuario que hace la solicitud
            String token = authHeader.substring(7);
            List<String> roles = jwtService.extraerRoles(token);

            if (!roles.contains("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.errorAccesoDenegado("Permisos insuficientes"));
            }

            authService.desbloquearCuenta(Math.toIntExact(usuarioId));

            return ResponseEntity.ok(
                    ApiResponse.exitoSinDatos("Usuario desbloqueado exitosamente")
            );

        } catch (Exception e) {
            logger.error("Error al desbloquear usuario: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al desbloquear usuario"));
        }
    }

    /**
     * Obtiene el historial de auditoría de un usuario
     * @param usuarioId ID del usuario
     * @param page Página (default: 0)
     * @param size Tamaño de página (default: 20)
     * @return Historial paginado de auditoría
     */
    @GetMapping("/auditoria/usuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<Auditoria>>> obtenerAuditoriaUsuario(
            @PathVariable Integer usuarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestHeader("Authorization") String authHeader) {

        logger.debug("Obteniendo auditoría para usuario ID: {}", usuarioId);

        try {
            // Verificar permisos
            String token = authHeader.substring(7);
            List<String> roles = jwtService.extraerRoles(token);

            if (!roles.contains("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.errorAccesoDenegado("Permisos insuficientes"));
            }

            Pageable pageable = PageRequest.of(page, size, Sort.by("fechaRegistro").descending());
            Page<Auditoria> auditoria = auditoriaService.obtenerHistorialUsuario(Math.toIntExact(usuarioId), pageable);

            return ResponseEntity.ok(
                    ApiResponse.exito(auditoria, "Historial de auditoría obtenido")
            );

        } catch (Exception e) {
            logger.error("Error al obtener auditoría: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener auditoría"));
        }
    }

    /**
     * Genera reporte de actividad del sistema
     * @param fechaInicio Fecha inicio del reporte
     * @param fechaFin Fecha fin del reporte
     * @return Reporte de actividad
     */
    @GetMapping("/reporte-actividad")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ReporteActividadResponse>> generarReporteActividad(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestHeader("Authorization") String authHeader) {

        logger.info("Generando reporte de actividad del {} al {}", fechaInicio, fechaFin);

        try {
            // Verificar permisos
            String token = authHeader.substring(7);
            List<String> roles = jwtService.extraerRoles(token);

            if (!roles.contains("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.errorAccesoDenegado("Permisos insuficientes"));
            }

            ReporteActividadResponse reporte = auditoriaService.generarReporteActividad(fechaInicio, fechaFin);

            return ResponseEntity.ok(
                    ApiResponse.exito(reporte, "Reporte generado exitosamente")
            );

        } catch (Exception e) {
            logger.error("Error al generar reporte: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al generar reporte"));
        }
    }

    /**
     * Obtiene los últimos intentos de login fallidos
     * @return Lista de intentos fallidos recientes
     */
    @GetMapping("/intentos-fallidos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Auditoria>>> obtenerIntentosFallidos(
            @RequestHeader("Authorization") String authHeader) {

        try {
            // Verificar permisos
            String token = authHeader.substring(7);
            List<String> roles = jwtService.extraerRoles(token);

            if (!roles.contains("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.errorAccesoDenegado("Permisos insuficientes"));
            }

            List<Auditoria> intentosFallidos = auditoriaService.obtenerUltimosLoginsFallidos();

            return ResponseEntity.ok(
                    ApiResponse.exito(intentosFallidos, "Intentos fallidos obtenidos")
            );

        } catch (Exception e) {
            logger.error("Error al obtener intentos fallidos: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener intentos fallidos"));
        }
    }

    /**
     * Obtiene estadísticas generales del sistema
     * @return Estadísticas del sistema
     */
//    @GetMapping("/estadisticas")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<ApiResponse<EstadisticasGeneralesResponse>> obtenerEstadisticasGenerales(
//            @RequestHeader("Authorization") String authHeader) {
//
//        try {
//            // Verificar permisos
//            String token = authHeader.substring(7);
//            List<String> roles = jwtService.extraerRoles(token);
//
//            if (!roles.contains("ADMIN")) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                        .body(ApiResponse.errorAccesoDenegado("Permisos insuficientes"));
//            }
//
//            // Aquí implementarías la lógica para obtener estadísticas generales
//            EstadisticasGeneralesResponse estadisticas = new EstadisticasGeneralesResponse();
//            estadisticas.setTotalUsuarios(100L); // Ejemplo - implementar lógica real
//            estadisticas.setUsuariosActivos(85L);
//            estadisticas.setLoginsDiaActual(25L);
//            estadisticas.setIntentosFallidosHoy(3L);
//
//            return ResponseEntity.ok(
//                    ApiResponse.exito(estadisticas, "Estadísticas obtenidas")
//            );
//
//        } catch (Exception e) {
//            logger.error("Error al obtener estadísticas: {}", e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(ApiResponse.error("Error al obtener estadísticas"));
//        }
//    }
}
