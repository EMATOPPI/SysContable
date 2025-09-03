package com.contaduria.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configuración para procesamiento asíncrono
 * Principalmente para auditoría que no debe bloquear las operaciones principales
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * Executor para tareas asíncronas como auditoría
     * Configurado para manejar múltiples operaciones concurrentemente
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // Mínimo 2 hilos
        executor.setMaxPoolSize(5); // Máximo 5 hilos
        executor.setQueueCapacity(100); // Cola de 100 tareas
        executor.setThreadNamePrefix("Auth-Async-"); // Prefijo para logs
        executor.initialize();
        return executor;
    }
}
