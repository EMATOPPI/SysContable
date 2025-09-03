package com.contaduria.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del servicio de autenticación
 * Punto de entrada de la aplicación Spring Boot
 */
@SpringBootApplication
public class AuthServiceApplication {

	public static void main(String[] args) {
		// Configurar propiedades del sistema para mejor logging
		System.setProperty("spring.output.ansi.enabled", "always");

		SpringApplication.run(AuthServiceApplication.class, args);
	}
}