package com.contaduria.auth.service;

import com.contaduria.auth.entity.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio para manejo de tokens JWT
 * Genera, valida y extrae información de tokens de acceso y refresh
 */
@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private Integer jwtExpiration;

    @Value("${app.jwt.refresh-expiration}")
    private Integer refreshExpiration;

    /**
     * Obtiene la clave de firma para los tokens
     * @return Clave secreta para firmar JWT
     */
    private SecretKey getClaveSecreta() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Genera un token de acceso JWT para el usuario
     * @param usuario Usuario autenticado
     * @return Token JWT como String
     */
    public String generarTokenAcceso(Usuario usuario) {
        logger.debug("Generando token de acceso para usuario: {}", usuario.getUsuario());

        Map<String, Object> claims = crearClaimsUsuario(usuario);

        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + jwtExpiration);

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(usuario.getUsuario())
                .setIssuedAt(ahora)
                .setExpiration(expiracion)
                .signWith(getClaveSecreta(), SignatureAlgorithm.HS512)
                .compact();

        logger.debug("Token de acceso generado exitosamente para usuario: {}", usuario.getUsuario());
        return token;
    }

    /**
     * Genera un refresh token para el usuario
     * @param usuario Usuario autenticado
     * @return Refresh token como String
     */
    public String generarRefreshToken(Usuario usuario) {
        logger.debug("Generando refresh token para usuario: {}", usuario.getUsuario());

        Map<String, Object> claims = new HashMap<>();
        claims.put("usuarioId", usuario.getIdUsuarios());
        claims.put("tipo", "refresh");

        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + refreshExpiration);

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(usuario.getUsuario())
                .setIssuedAt(ahora)
                .setExpiration(expiracion)
                .signWith(getClaveSecreta(), SignatureAlgorithm.HS512)
                .compact();

        logger.debug("Refresh token generado exitosamente para usuario: {}", usuario.getUsuario());
        return token;
    }

    /**
     * Crea los claims del usuario para incluir en el JWT
     * @param usuario Usuario del cual extraer información
     * @return Map con los claims del usuario
     */
    private Map<String, Object> crearClaimsUsuario(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();

        // Información básica del usuario
        claims.put("usuarioId", usuario.getIdUsuarios());
        claims.put("empleadoId", usuario.getIdEmpleados());
        claims.put("nombreCompleto", usuario.getNombreCompleto());
        claims.put("email", usuario.getEmail());

        // Roles del usuario
        List<String> roles = usuario.getRoles().stream()
                .map(rol -> rol.getNombre())
                .collect(Collectors.toList());
        claims.put("roles", roles);

        // Permisos del usuario (menús que puede ver)
        List<String> permisos = usuario.getRoles().stream()
                .flatMap(rol -> rol.getPermisos().stream())
                .filter(permiso -> permiso.puedeVer())
                .map(permiso -> permiso.getMenu().getNombre())
                .distinct()
                .collect(Collectors.toList());
        claims.put("permisos", permisos);

        // Información del empleado
        if (usuario.getEmpleado() != null) {
            claims.put("puedeVerTodosClientes", usuario.getEmpleado().getVerTodosClientes());
            claims.put("estadoEmpleado", usuario.getEmpleado().getEstado());
        }

        return claims;
    }

    /**
     * Valida si un token es válido
     * @param token Token a validar
     * @return true si es válido, false si no
     */
    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getClaveSecreta())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            logger.warn("Token inválido: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Error al validar token: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Extrae el nombre de usuario del token
     * @param token Token del cual extraer el usuario
     * @return Nombre de usuario
     */
    public String extraerUsuario(String token) {
        return extraerClaims(token).getSubject();
    }

    /**
     * Extrae el ID del usuario del token
     * @param token Token del cual extraer el ID
     * @return ID del usuario
     */
    public Integer extraerUsuarioId(String token) {
        Claims claims = extraerClaims(token);
        return Integer.valueOf(claims.get("usuarioId", Integer.class));
    }

    /**
     * Extrae el ID del empleado del token
     * @param token Token del cual extraer el ID
     * @return ID del empleado
     */
    public Integer extraerEmpleadoId(String token) {
        Claims claims = extraerClaims(token);
        return Integer.valueOf(claims.get("empleadoId", Integer.class));
    }

    /**
     * Extrae los roles del token
     * @param token Token del cual extraer los roles
     * @return Lista de roles
     */
    @SuppressWarnings("unchecked")
    public List<String> extraerRoles(String token) {
        Claims claims = extraerClaims(token);
        return (List<String>) claims.get("roles");
    }

    /**
     * Extrae los permisos del token
     * @param token Token del cual extraer los permisos
     * @return Lista de permisos
     */
    @SuppressWarnings("unchecked")
    public List<String> extraerPermisos(String token) {
        Claims claims = extraerClaims(token);
        return (List<String>) claims.get("permisos");
    }

    /**
     * Extrae la fecha de expiración del token
     * @param token Token del cual extraer la fecha
     * @return Fecha de expiración
     */
    public LocalDateTime extraerExpiracion(String token) {
        Date expiration = extraerClaims(token).getExpiration();
        return expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
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
            logger.warn("Error al verificar expiración del token: {}", e.getMessage());
            return true;
        }
    }

    /**
     * Extrae todos los claims del token
     * @param token Token del cual extraer los claims
     * @return Claims del token
     */
    private Claims extraerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getClaveSecreta())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Obtiene el tiempo de expiración configurado
     * @return Tiempo de expiración en milisegundos
     */
    public Integer obtenerTiempoExpiracion() {
        return jwtExpiration;
    }

    /**
     * Verifica si un token es de tipo refresh
     * @param token Token a verificar
     * @return true si es refresh token, false si no
     */
    public boolean esRefreshToken(String token) {
        try {
            Claims claims = extraerClaims(token);
            return "refresh".equals(claims.get("tipo"));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extrae información completa del usuario desde el token
     * @param token Token del cual extraer la información
     * @return Map con toda la información del usuario
     */
    public Map<String, Object> extraerInformacionCompleta(String token) {
        Claims claims = extraerClaims(token);
        Map<String, Object> info = new HashMap<>();

        info.put("usuarioId", claims.get("usuarioId"));
        info.put("empleadoId", claims.get("empleadoId"));
        info.put("usuario", claims.getSubject());
        info.put("nombreCompleto", claims.get("nombreCompleto"));
        info.put("email", claims.get("email"));
        info.put("roles", claims.get("roles"));
        info.put("permisos", claims.get("permisos"));
        info.put("puedeVerTodosClientes", claims.get("puedeVerTodosClientes"));
        info.put("expiracion", extraerExpiracion(token));

        return info;
    }
}