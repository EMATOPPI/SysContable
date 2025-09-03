package com.contaduria.service.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor para agregar información del usuario al contexto de logging
 * Permite que todos los logs incluyan información del usuario autenticado
 */
@Component
public class UserContextInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(UserContextInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // Extraer información del usuario de los headers
        String usuarioId = request.getHeader("X-Usuario-Id");
        String nombreUsuario = request.getHeader("X-Usuario-Nombre");
        String empleadoId = request.getHeader("X-Empleado-Id");
        String roles = request.getHeader("X-Usuario-Roles");

        // Agregar al contexto de logging (MDC)
        if (usuarioId != null) {
            MDC.put("usuarioId", usuarioId);
        }
        if (nombreUsuario != null) {
            MDC.put("usuario", nombreUsuario);
        }
        if (empleadoId != null) {
            MDC.put("empleadoId", empleadoId);
        }
        if (roles != null) {
            MDC.put("roles", roles);
        }

        logger.debug("Usuario {} (ID: {}) accediendo a: {} {}",
                nombreUsuario, usuarioId, request.getMethod(), request.getRequestURI());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        // Limpiar el contexto MDC después de la request
        MDC.clear();
    }
}
