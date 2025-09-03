package com.contaduria.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Servicio para manejo de encriptación de contraseñas
 * Soporta tanto el sistema anterior como BCrypt para migración gradual
 */
@Service
public class EncriptacionService {

    private static final Logger logger = LoggerFactory.getLogger(EncriptacionService.class);

    // BCrypt para nuevas contraseñas (más seguro)
    private final BCryptPasswordEncoder bcryptEncoder;

    // Clave para el sistema anterior (debe coincidir con el existente)
    private static final String CLAVE_ANTIGUA = "password123"; // Cambiar por la clave real
    private static final String ALGORITMO_ANTIGUO = "AES";

    public EncriptacionService() {
        this.bcryptEncoder = new BCryptPasswordEncoder(12);
    }

    /**
     * Encripta una contraseña usando BCrypt (sistema nuevo)
     * @param contrasenaPlana Contraseña en texto plano
     * @return Contraseña encriptada con BCrypt
     */
    public String encriptarNuevaContrasena(String contrasenaPlana) {
        logger.debug("Encriptando nueva contraseña con BCrypt");
        return bcryptEncoder.encode(contrasenaPlana);
    }

    /**
     * Valida una contraseña contra su hash
     * Soporta tanto BCrypt como el sistema anterior
     * @param contrasenaPlana Contraseña en texto plano
     * @param contrasenaEncriptada Contraseña encriptada almacenada
     * @return true si coinciden, false si no
     */
    public boolean validarContrasena(String contrasenaPlana, String contrasenaEncriptada) {
        try {
            // Si la contraseña empieza con $2a$, es BCrypt
            if (contrasenaEncriptada.startsWith("$2a$") || contrasenaEncriptada.startsWith("$2b$")) {
                logger.debug("Validando contraseña con BCrypt");
                return bcryptEncoder.matches(contrasenaPlana, contrasenaEncriptada);
            }
            // Si no, usar el sistema anterior
            else {
                logger.debug("Validando contraseña con sistema anterior");
                return validarContrasenaAnterior(contrasenaPlana, contrasenaEncriptada);
            }
        } catch (Exception e) {
            logger.error("Error al validar contraseña: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Valida contraseña usando el método anterior del sistema
     * @param contrasenaPlana Contraseña en texto plano
     * @param contrasenaEncriptada Contraseña encriptada con método anterior
     * @return true si coinciden, false si no
     */
    private boolean validarContrasenaAnterior(String contrasenaPlana, String contrasenaEncriptada) {
        try {
            String contrasenaDesencriptada = desencriptarSistemaAnterior(contrasenaEncriptada);
            return contrasenaPlana.equals(contrasenaDesencriptada);
        } catch (Exception e) {
            logger.error("Error al validar contraseña con sistema anterior: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Desencripta una contraseña usando el método anterior
     * @param contrasenaEncriptada Contraseña encriptada
     * @return Contraseña desencriptada
     */
    private String desencriptarSistemaAnterior(String contrasenaEncriptada) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(CLAVE_ANTIGUA.getBytes(), ALGORITMO_ANTIGUO);
        Cipher cipher = Cipher.getInstance(ALGORITMO_ANTIGUO);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] encrypted = Base64.getDecoder().decode(contrasenaEncriptada);
        byte[] decrypted = cipher.doFinal(encrypted);

        return new String(decrypted);
    }

    /**
     * Verifica si una contraseña ya está encriptada con BCrypt
     * @param contrasena Contraseña a verificar
     * @return true si es BCrypt, false si no
     */
    public boolean esBCrypt(String contrasena) {
        return contrasena != null &&
                (contrasena.startsWith("$2a$") || contrasena.startsWith("$2b$"));
    }

    /**
     * Migra una contraseña del sistema anterior a BCrypt
     * @param contrasenaPlana Contraseña en texto plano
     * @return Contraseña encriptada con BCrypt
     */
    public String migrarABCrypt(String contrasenaPlana) {
        logger.info("Migrando contraseña a BCrypt");
        return encriptarNuevaContrasena(contrasenaPlana);
    }

    /**
     * Verifica la fortaleza de una contraseña
     * @param contrasena Contraseña a verificar
     * @return true si es fuerte, false si no
     */
    public boolean esContrasenaFuerte(String contrasena) {
        if (contrasena == null || contrasena.length() < 6) {
            return false;
        }

        boolean tieneMayuscula = contrasena.chars().anyMatch(Character::isUpperCase);
        boolean tieneMinuscula = contrasena.chars().anyMatch(Character::isLowerCase);
        boolean tieneNumero = contrasena.chars().anyMatch(Character::isDigit);
        boolean tieneCaracterEspecial = contrasena.chars()
                .anyMatch(ch -> "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(ch) >= 0);

        // Al menos 3 de los 4 criterios
        int criteriosCumplidos = 0;
        if (tieneMayuscula) criteriosCumplidos++;
        if (tieneMinuscula) criteriosCumplidos++;
        if (tieneNumero) criteriosCumplidos++;
        if (tieneCaracterEspecial) criteriosCumplidos++;

        return criteriosCumplidos >= 3;
    }

    /**
     * Genera recomendaciones para mejorar la contraseña
     * @param contrasena Contraseña a evaluar
     * @return Lista de recomendaciones
     */
    public String obtenerRecomendacionesContrasena(String contrasena) {
        if (contrasena == null) {
            return "La contraseña es requerida";
        }

        StringBuilder recomendaciones = new StringBuilder();

        if (contrasena.length() < 6) {
            recomendaciones.append("Debe tener al menos 6 caracteres. ");
        }

        if (!contrasena.chars().anyMatch(Character::isUpperCase)) {
            recomendaciones.append("Debe incluir al menos una letra mayúscula. ");
        }

        if (!contrasena.chars().anyMatch(Character::isLowerCase)) {
            recomendaciones.append("Debe incluir al menos una letra minúscula. ");
        }

        if (!contrasena.chars().anyMatch(Character::isDigit)) {
            recomendaciones.append("Debe incluir al menos un número. ");
        }

        if (!contrasena.chars().anyMatch(ch -> "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(ch) >= 0)) {
            recomendaciones.append("Se recomienda incluir caracteres especiales (!@#$%^&*). ");
        }

        return recomendaciones.length() > 0 ? recomendaciones.toString().trim() : "Contraseña cumple los criterios de seguridad";
    }
}
