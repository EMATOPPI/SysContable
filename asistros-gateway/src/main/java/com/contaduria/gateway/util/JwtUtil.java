package com.contaduria.gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * Utilidad para manejo de JWT en el Gateway
 * Se enfoca solo en validación y extracción de datos
 */
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${app.jwt.secret}")
    private String secret;

    /**
     * Obtiene la clave de firma para validar tokens
     * @return Clave secreta
     */
    private SecretKey getClaveSecreta() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Valida si un token es válido
     * @param token Token a validar
     * @return true si es válido, false si no
     */
    public boolean esTokenValido(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getClaveSecreta())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            logger.debug("Token inválido: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si un token ha expirado
     * @param token Token a verificar
     * @return true si ha expirado, false si no
     */
    public boolean tokenExpirado(String token) {
        try {
            Date expiration = extraerClaims(token).getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            logger.debug("Error al verificar expiración: {}", e.getMessage());
            return true;
        }
    }

    /**
     * Extrae el ID del usuario del token
     * @param token Token JWT
     * @return ID del usuario como String
     */
    public String extraerUsuarioId(String token) {
        Claims claims = extraerClaims(token);
        Object usuarioId = claims.get("usuarioId");
        return usuarioId != null ? usuarioId.toString() : "0";
    }

    /**
     * Extrae el ID del empleado del token
     * @param token Token JWT
     * @return ID del empleado como String
     */
    public String extraerEmpleadoId(String token) {
        Claims claims = extraerClaims(token);
        Object empleadoId = claims.get("empleadoId");
        return empleadoId != null ? empleadoId.toString() : "0";
    }

    /**
     * Extrae el nombre de usuario del token
     * @param token Token JWT
     * @return Nombre de usuario
     */
    public String extraerUsuario(String token) {
        return extraerClaims(token).getSubject();
    }

    /**
     * Extrae el nombre completo del usuario del token
     * @param token Token JWT
     * @return Nombre completo
     */
    public String extraerNombreCompleto(String token) {
        Claims claims = extraerClaims(token);
        return claims.get("nombreCompleto", String.class);
    }

    /**
     * Extrae el email del usuario del token
     * @param token Token JWT
     * @return Email del usuario
     */
    public String extraerEmail(String token) {
        Claims claims = extraerClaims(token);
        return claims.get("email", String.class);
    }

    /**
     * Extrae los roles del usuario del token
     * @param token Token JWT
     * @return Lista de roles
     */
    @SuppressWarnings("unchecked")
    public List<String> extraerRoles(String token) {
        Claims claims = extraerClaims(token);
        return (List<String>) claims.get("roles");
    }

    /**
     * Extrae los permisos del usuario del token
     * @param token Token JWT
     * @return Lista de permisos
     */
    @SuppressWarnings("unchecked")
    public List<String> extraerPermisos(String token) {
        Claims claims = extraerClaims(token);
        return (List<String>) claims.get("permisos");
    }

    /**
     * Extrae si el usuario puede ver todos los clientes
     * @param token Token JWT
     * @return Boolean indicando si puede ver todos los clientes
     */
    public Boolean extraerPuedeVerTodosClientes(String token) {
        Claims claims = extraerClaims(token);
        return claims.get("puedeVerTodosClientes", Boolean.class);
    }

    /**
     * Extrae todos los claims del token
     * @param token Token JWT
     * @return Claims del token
     */
    private Claims extraerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getClaveSecreta())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
