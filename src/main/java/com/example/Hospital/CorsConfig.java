package com.example.Hospital;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Libera todas as rotas
                        .allowedOrigins("http://127.0.0.1:5500") // Frontend local
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos liberados
                        .allowedHeaders("*"); // Permite qualquer header
            }
        };
    }
}
