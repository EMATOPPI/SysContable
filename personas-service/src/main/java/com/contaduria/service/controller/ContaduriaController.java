package com.contaduria.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador principal del servicio de contaduría
 * Recibe headers del Gateway con información del usuario autenticado
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ContaduriaController {

    private static final Logger logger = LoggerFactory.getLogger(ContaduriaController.class);

    /**
     * Health check del servicio
     */
    @GetMapping("/contaduria/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("estado", "ACTIVO");
        health.put("servicio", "contaduria-service");
        health.put("version", "1.0.0");
        health.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(health);
    }

    /**
     * Ejemplo de endpoint protegido que recibe información del usuario
     */
    @GetMapping("/clientes")
    public ResponseEntity<Map<String, Object>> obtenerClientes(
            @RequestHeader(value = "X-Usuario-Id", required = false) String usuarioId,
            @RequestHeader(value = "X-Empleado-Id", required = false) String empleadoId,
            @RequestHeader(value = "X-Usuario-Nombre", required = false) String nombreUsuario,
            @RequestHeader(value = "X-Usuario-Nombre-Completo", required = false) String nombreCompleto,
            @RequestHeader(value = "X-Usuario-Email", required = false) String email,
            @RequestHeader(value = "X-Usuario-Roles", required = false) String roles,
            @RequestHeader(value = "X-Usuario-Permisos", required = false) String permisos,
            @RequestHeader(value = "X-Puede-Ver-Todos-Clientes", required = false) String puedeVerTodosClientes) {

        logger.info("=== ACCESO A CLIENTES ===");
        logger.info("Usuario ID: {}", usuarioId);
        logger.info("Empleado ID: {}", empleadoId);
        logger.info("Nombre Usuario: {}", nombreUsuario);
        logger.info("Nombre Completo: {}", nombreCompleto);
        logger.info("Email: {}", email);
        logger.info("Roles: {}", roles);
        logger.info("Permisos: {}", permisos);
        logger.info("Puede ver todos clientes: {}", puedeVerTodosClientes);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Lista de clientes");
        response.put("usuarioAutenticado", nombreCompleto);
        response.put("roles", roles);
        response.put("puedeVerTodos", puedeVerTodosClientes);
        response.put("timestamp", LocalDateTime.now());

        // Aquí implementarías la lógica real para obtener clientes
        // basada en los permisos del usuario

        return ResponseEntity.ok(response);
    }

    /**
     * Ejemplo de endpoint para empresas
     */
    @GetMapping("/empresas")
    public ResponseEntity<Map<String, Object>> obtenerEmpresas(
            @RequestHeader(value = "X-Usuario-Id", required = false) String usuarioId,
            @RequestHeader(value = "X-Usuario-Roles", required = false) String roles) {

        logger.info("Acceso a empresas - Usuario: {} - Roles: {}", usuarioId, roles);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Lista de empresas");
        response.put("usuarioId", usuarioId);
        response.put("roles", roles);

        return ResponseEntity.ok(response);
    }

    /**
     * Ejemplo de endpoint para documentos
     */
    @GetMapping("/documentos")
    public ResponseEntity<Map<String, Object>> obtenerDocumentos(
            @RequestHeader(value = "X-Usuario-Id", required = false) String usuarioId,
            @RequestHeader(value = "X-Usuario-Permisos", required = false) String permisos) {

        logger.info("Acceso a documentos - Usuario: {} - Permisos: {}", usuarioId, permisos);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Lista de documentos");
        response.put("usuarioId", usuarioId);
        response.put("permisos", permisos);

        return ResponseEntity.ok(response);
    }

    /**
     * Ejemplo de endpoint para reportes
     */
    @GetMapping("/reportes")
    public ResponseEntity<Map<String, Object>> obtenerReportes(
            @RequestHeader(value = "X-Usuario-Id", required = false) String usuarioId,
            @RequestHeader(value = "X-Usuario-Roles", required = false) String roles) {

        logger.info("Acceso a reportes - Usuario: {} - Roles: {}", usuarioId, roles);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Lista de reportes");
        response.put("usuarioId", usuarioId);
        response.put("roles", roles);

        return ResponseEntity.ok(response);
    }

    /**
     * Ejemplo de endpoint para facturas
     */
    @PostMapping("/facturas")
    public ResponseEntity<Map<String, Object>> crearFactura(
            @RequestHeader(value = "X-Usuario-Id", required = false) String usuarioId,
            @RequestHeader(value = "X-Empleado-Id", required = false) String empleadoId,
            @RequestBody Map<String, Object> facturaData) {

        logger.info("Creando factura - Usuario: {} - Empleado: {}", usuarioId, empleadoId);
        logger.info("Datos de factura: {}", facturaData);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Factura creada exitosamente");
        response.put("usuarioCreador", usuarioId);
        response.put("empleadoCreador", empleadoId);
        response.put("facturaId", "FACT-001");
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(response);
    }

    /**
     * Ejemplo de endpoint para balances
     */
    @GetMapping("/balances")
    public ResponseEntity<Map<String, Object>> obtenerBalances(
            @RequestHeader(value = "X-Usuario-Id", required = false) String usuarioId,
            @RequestHeader(value = "X-Puede-Ver-Todos-Clientes", required = false) String puedeVerTodos) {

        logger.info("Acceso a balances - Usuario: {} - Puede ver todos: {}", usuarioId, puedeVerTodos);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Lista de balances");
        response.put("usuarioId", usuarioId);
        response.put("accesoCompleto", "true".equals(puedeVerTodos));

        return ResponseEntity.ok(response);
    }
}
