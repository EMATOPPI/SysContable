package com.contaduria.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para health check del Gateway
 */
@RestController
@RequestMapping("/api/gateway")
public class GatewayController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("estado", "ACTIVO");
        health.put("servicio", "contaduria-gateway");
        health.put("version", "1.0.0");
        health.put("timestamp", LocalDateTime.now());
        health.put("descripcion", "Gateway para sistema de contaduría pública");

        return ResponseEntity.ok(health);
    }
}