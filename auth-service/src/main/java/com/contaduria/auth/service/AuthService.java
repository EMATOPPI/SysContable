package com.contaduria.auth.service;

import com.contaduria.auth.dto.request.LoginRequest;
import com.contaduria.auth.dto.request.CambioContrasenaRequest;
import com.contaduria.auth.dto.response.*;
import com.contaduria.auth.entity.Usuario;
import com.contaduria.auth.entity.Menu;
import com.contaduria.auth.entity.Permiso;
import com.contaduria.auth.repository.UsuarioRepository;
import com.contaduria.auth.repository.MenuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio principal de autenticación
 * Maneja login, logout, validación de tokens y gestión de sesiones
 */
@Service
@Transactional
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EncriptacionService encriptacionService;

    @Autowired
    private AuditoriaService auditoriaService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Autentica un usuario y genera tokens de acceso
     * @param request Datos de login del usuario
     * @return Respuesta con tokens y información del usuario
     */
    public LoginResponse login(LoginRequest request) {
        logger.info("Intento de login para usuario: {}", request.getUsuario());

        try {
            // Buscar usuario en la base de datos
            Usuario usuario = usuarioRepository.findUsuarioCompletoByUsuario(request.getUsuario())
                    .orElseThrow(() -> new BadCredentialsException("Usuario no encontrado"));

            // Verificar si la cuenta está activa
//            if (!usuario.puedeAcceder()) {
//                auditoriaService.registrarAccesoFallido((int) usuario.getIdUsuarios(),
//                        "Cuenta inactiva o bloqueada", request.getUsuario());
//
//                if (Boolean.TRUE.equals(usuario.getCuentaBloqueada())) {
//                    throw new LockedException("Cuenta bloqueada por múltiples intentos fallidos");
//                } else {
//                    throw new DisabledException("Cuenta desactivada");
//                }
//            }

            // Validar contraseña
            if (!encriptacionService.validarContrasena(request.getContrasena(), usuario.getContrasena())) {
                // Registrar intento fallido
//                usuario.registrarAccesoFallido();
                usuarioRepository.save(usuario);

                auditoriaService.registrarAccesoFallido(usuario.getIdUsuarios(),
                        "Contraseña incorrecta", request.getUsuario());

                throw new BadCredentialsException("Contraseña incorrecta");
            }

            // Migrar contraseña a BCrypt si es necesario
            if (!encriptacionService.esBCrypt(usuario.getContrasena())) {
                logger.info("Migrando contraseña a BCrypt para usuario: {}", usuario.getUsuario());
                String nuevaContrasena = encriptacionService.migrarABCrypt(request.getContrasena());
                usuario.setContrasena(nuevaContrasena);
            }

            // Registrar acceso exitoso
//            usuario.registrarAccesoExitoso();
            usuarioRepository.save(usuario);

            // Generar tokens
            String accessToken = jwtService.generarTokenAcceso(usuario);
            String refreshToken = jwtService.generarRefreshToken(usuario);

            // Crear respuesta
            LoginResponse response = new LoginResponse();
            response.setAccessToken(accessToken);
            response.setRefreshToken(refreshToken);
            response.setExpiraEn(jwtService.obtenerTiempoExpiracion());
            response.setUsuario(mapearUsuarioResponse(usuario));
            response.setRoles(extraerRoles(usuario));
            response.setMenusPermitidos(obtenerMenusPermitidos(usuario));

            // Registrar en auditoría
            auditoriaService.registrarAccesoExitoso(usuario.getIdUsuarios(),
                    "Login exitoso", request.getUsuario());

            logger.info("Login exitoso para usuario: {}", request.getUsuario());
            return response;

        } catch (AuthenticationException e) {
            logger.warn("Fallo de autenticación para usuario: {} - {}", request.getUsuario(), e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error inesperado durante login para usuario: {}", request.getUsuario(), e);
            throw new RuntimeException("Error interno del servidor durante autenticación");
        }
    }

    /**
     * Valida un token de acceso
     * @param token Token a validar
     * @return Información del usuario si el token es válido
     */
    public ValidacionTokenResponse validarToken(String token) {
        logger.debug("Validando token de acceso");

        try {
            if (!jwtService.validarToken(token)) {
                return ValidacionTokenResponse.tokenInvalido("Token inválido o expirado");
            }

            if (jwtService.tokenExpirado(token)) {
                return ValidacionTokenResponse.tokenInvalido("Token expirado");
            }

            // Extraer información del token
            Map<String, Object> info = jwtService.extraerInformacionCompleta(token);

            // Verificar que el usuario sigue activo
            Integer usuarioId = (Integer) info.get("usuarioId");
            Usuario usuario = usuarioRepository.findById(Math.toIntExact(usuarioId))
                    .orElse(null);

//            if (usuario == null || !usuario.puedeAcceder()) {
//                return ValidacionTokenResponse.tokenInvalido("Usuario inactivo");
//            }

            return ValidacionTokenResponse.tokenValido(
                    usuarioId,
                    (String) info.get("usuario"),
                    (String) info.get("nombreCompleto"),
                    (Integer) info.get("empleadoId"),
                    (List<String>) info.get("roles"),
                    (List<String>) info.get("permisos"),
                    (int) info.get("puedeVerTodosClientes"),
                    (LocalDateTime) info.get("expiracion")
            );

        } catch (Exception e) {
            logger.error("Error al validar token: {}", e.getMessage(), e);
            return ValidacionTokenResponse.tokenInvalido("Error al procesar token");
        }
    }

    /**
     * Renueva un token de acceso usando un refresh token
     * @param refreshToken Refresh token válido
     * @return Nuevo token de acceso
     */
    public LoginResponse renovarToken(String refreshToken) {
        logger.debug("Renovando token de acceso");

        try {
            if (!jwtService.validarToken(refreshToken) || !jwtService.esRefreshToken(refreshToken)) {
                throw new BadCredentialsException("Refresh token inválido");
            }

            String usuario = jwtService.extraerUsuario(refreshToken);
            Usuario usuarioEntity = usuarioRepository.findUsuarioCompletoByUsuario(usuario)
                    .orElseThrow(() -> new BadCredentialsException("Usuario no encontrado"));

//            if (!usuarioEntity.puedeAcceder()) {
//                throw new DisabledException("Usuario inactivo");
//            }

            // Generar nuevo access token
            String nuevoAccessToken = jwtService.generarTokenAcceso(usuarioEntity);

            LoginResponse response = new LoginResponse();
            response.setAccessToken(nuevoAccessToken);
            response.setRefreshToken(refreshToken); // Mantener el mismo refresh token
            response.setExpiraEn(jwtService.obtenerTiempoExpiracion());
            response.setUsuario(mapearUsuarioResponse(usuarioEntity));

            logger.debug("Token renovado exitosamente para usuario: {}", usuario);
            return response;

        } catch (Exception e) {
            logger.error("Error al renovar token: {}", e.getMessage(), e);
            throw new BadCredentialsException("No se pudo renovar el token");
        }
    }

    /**
     * Cambia la contraseña de un usuario
     * @param usuarioId ID del usuario
     * @param request Datos del cambio de contraseña
     */
    public void cambiarContrasena(Integer usuarioId, CambioContrasenaRequest request) {
        logger.info("Cambio de contraseña solicitado para usuario ID: {}", usuarioId);

        // Validar que las contraseñas coincidan
        if (!request.getNuevaContrasena().equals(request.getConfirmarContrasena())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar contraseña actual
        if (!encriptacionService.validarContrasena(request.getContrasenaActual(), usuario.getContrasena())) {
            auditoriaService.registrarCambioContrasenaFallido(usuarioId, "Contraseña actual incorrecta");
            throw new BadCredentialsException("Contraseña actual incorrecta");
        }

        // Validar fortaleza de la nueva contraseña
        if (!encriptacionService.esContrasenaFuerte(request.getNuevaContrasena())) {
            String recomendaciones = encriptacionService.obtenerRecomendacionesContrasena(request.getNuevaContrasena());
            throw new IllegalArgumentException("Contraseña débil. " + recomendaciones);
        }

        // Encriptar nueva contraseña con BCrypt
        String nuevaContrasenaEncriptada = encriptacionService.encriptarNuevaContrasena(request.getNuevaContrasena());
        usuario.setContrasena(nuevaContrasenaEncriptada);

        usuarioRepository.save(usuario);

        auditoriaService.registrarCambioContrasenaExitoso(usuarioId, "Contraseña cambiada exitosamente");
        logger.info("Contraseña cambiada exitosamente para usuario ID: {}", usuarioId);
    }

    /**
     * Obtiene el perfil completo del usuario
     * @param usuarioId ID del usuario
     * @return Información completa del perfil
     */
    @Transactional(readOnly = true)
    public PerfilUsuarioResponse obtenerPerfil(Integer usuarioId) {
        logger.debug("Obteniendo perfil para usuario ID: {}", usuarioId);

        Usuario usuario = usuarioRepository.findUsuarioCompletoByUsuario(
                usuarioRepository.findById(usuarioId)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                        .getUsuario()
        ).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        PerfilUsuarioResponse perfil = new PerfilUsuarioResponse();
        perfil.setUsuario(mapearUsuarioResponse(usuario));
        perfil.setRoles(mapearRoles(usuario));
        perfil.setMenusPermitidos(obtenerMenusPermitidos(usuario));
//        perfil.setUltimaActividad(usuario.getUltimoAcceso());

        return perfil;
    }

    /**
     * Desbloquea una cuenta de usuario
     * @param usuarioId ID del usuario a desbloquear
     */
    public void desbloquearCuenta(Integer usuarioId) {
        logger.info("Desbloqueando cuenta para usuario ID: {}", usuarioId);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

//        usuario.setCuentaBloqueada(false);
//        usuario.setIntentosFallidos(0);
//        usuario.setFechaBloqueo(null);

        usuarioRepository.save(usuario);

        auditoriaService.registrarDesbloqueoCuenta(usuarioId, "Cuenta desbloqueada manualmente");
        logger.info("Cuenta desbloqueada exitosamente para usuario ID: {}", usuarioId);
    }

    // ========================================
    // MÉTODOS PRIVADOS DE UTILIDAD
    // ========================================

    private UsuarioResponse mapearUsuarioResponse(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();
        response.setIdUsuarios(usuario.getIdUsuarios());
        response.setUsuario(usuario.getUsuario());
        response.setNombreCompleto(usuario.getNombreCompleto());
        response.setEmail(usuario.getEmail());
        response.setActivo(usuario.getActivo());
//        response.setUltimoAcceso(usuario.getUltimoAcceso());
        response.setRoles(extraerRoles(usuario));

        if (usuario.getEmpleado() != null) {
            response.setPuedeVerTodosClientes(usuario.getEmpleado().getVerTodosClientes());
            response.setEmpleado(mapearEmpleadoResponse(usuario.getEmpleado()));
        }

        return response;
    }

    private EmpleadoResponse mapearEmpleadoResponse(com.contaduria.auth.entity.Empleado empleado) {
        EmpleadoResponse response = new EmpleadoResponse();
        response.setIdEmpleados(empleado.getIdEmpleados());
        response.setTelefono(empleado.getTelefono());
        response.setEstado(empleado.getEstado());
        response.setFechaDesde(empleado.getFechaDesde());
        response.setFechaNac(empleado.getFechaNac());
        response.setVerTodosClientes(empleado.getVerTodosClientes());

        if (empleado.getPersona() != null) {
            response.setPersona(mapearPersonaResponse(empleado.getPersona()));
        }

        return response;
    }

    private PersonaResponse mapearPersonaResponse(com.contaduria.auth.entity.Persona persona) {
        PersonaResponse response = new PersonaResponse();
        response.setIdPersonas(persona.getIdPersonas());
        response.setNombre(persona.getNombre());
        response.setApellido(persona.getApellido());
        response.setCorreo(persona.getCorreo());
        response.setTelefono(persona.getTelefono());
        response.setCel(persona.getCel());
        response.setCiudad(persona.getCiudad());
        response.setDepartamento(persona.getDepartamento());
        response.setCi(persona.getCi());
        return response;
    }

    private List<String> extraerRoles(Usuario usuario) {
        return usuario.getRoles().stream()
                .map(rol -> rol.getNombre())
                .collect(Collectors.toList());
    }

    private List<RolResponse> mapearRoles(Usuario usuario) {
        return usuario.getRoles().stream()
                .map(rol -> {
                    RolResponse response = new RolResponse();
                    response.setIdRoles(rol.getIdRoles());
                    response.setNombre(rol.getNombre());
//                    response.setDescripcion(rol.getDescripcion());
//                    response.setActivo(rol.getActivo());
                    return response;
                })
                .collect(Collectors.toList());
    }

    private List<MenuResponse> obtenerMenusPermitidos(Usuario usuario) {
        return usuario.getRoles().stream()
                .flatMap(rol -> rol.getPermisos().stream())
                .filter(Permiso::puedeVer)
                .map(permiso -> {
                    Menu menu = permiso.getMenu();
                    MenuResponse response = new MenuResponse();
                    response.setIdMenus(menu.getIdMenus());
                    response.setNombre(menu.getNombre());
//                    response.setDescripcion(menu.getDescripcion());
//                    response.setUrl(menu.getUrl());
//                    response.setIcono(menu.getIcono());
//                    response.setOrden(menu.getOrden());
                    response.setPuedeVer(true);
                    return response;
                })
                .distinct()
                .sorted((m1, m2) -> Integer.compare(
                        m1.getOrden() != null ? m1.getOrden() : 0,
                        m2.getOrden() != null ? m2.getOrden() : 0
                ))
                .collect(Collectors.toList());
    }
}
