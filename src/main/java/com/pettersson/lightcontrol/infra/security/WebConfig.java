package com.pettersson.lightcontrol.infra.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Esto permite todas las rutas
                .allowedOrigins("http://localhost:4200", "https://localhost:4200", "https://light-control-app-i3fb.onrender.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")  // Añadido PATCH
                .allowedHeaders("Content-Type", "Authorization")  // Especificar los encabezados
                .allowCredentials(true);  // Permitir cookies si se necesita
    }
}
