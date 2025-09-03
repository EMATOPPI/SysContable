package com.contaduria.auth.service;

import com.contaduria.auth.entity.Usuario;
import com.contaduria.auth.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio que implementa UserDetailsService para Spring Security
 * Carga los datos del usuario para el proceso de autenticación
 */
@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Carga un usuario por su nombre de usuario para Spring Security
     * @param username Nombre de usuario a buscar
     * @return UserDetails con información del usuario
     * @throws UsernameNotFoundException Si el usuario no existe
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("=== CARGANDO USUARIO PARA AUTENTICACIÓN ===");
        logger.debug("Buscando usuario: {}", username);

        // Buscar usuario con toda la información necesaria
        Usuario usuario = usuarioRepository.findUsuarioCompletoByUsuario(username)
                .orElseThrow(() -> {
                    logger.warn("Usuario no encontrado: {}", username);
                    return new UsernameNotFoundException("Usuario no encontrado: " + username);
                });

        logger.debug("Usuario encontrado: {}", usuario.getUsuario());
        logger.debug("Estado activo: {}", usuario.getActivo());
//        logger.debug("Cuenta bloqueada: {}", usuario.getCuentaBloqueada());
        logger.debug("Roles asignados: {}", usuario.getRoles().size());

        // Verificar estado de la cuenta
        if (usuario.getActivo() == 1) {
            return usuario;
        }
        logger.warn("Usuario inactivo: {}", username);
        throw new UsernameNotFoundException("Usuario inactivo: " + username);

        // Log de roles para debugging
//        usuario.getRoles().forEach(rol ->
//                logger.debug("Rol: {} - Activo: {}", rol.getNombre(), rol.getActivo())
//        );

    }
}
