package com.pettersson.lightcontrol.infra.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Esto permite todas las rutas
                .allowedOrigins("http://localhost:4200", "https://localhost:4200", "https://43ed-191-156-227-41.ngrok-free.app")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")  // Añadido PATCH
                .allowedHeaders("Content-Type", "Authorization")  // Especificar los encabezados
                .allowCredentials(true);  // Permitir cookies si se necesita
    }
}
