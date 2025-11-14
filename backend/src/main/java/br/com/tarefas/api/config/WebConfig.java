package br.com.tarefas.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        // Durante desenvolvimento aceitamos padrões para localhost nas portas do Vite
        registry.addMapping("/**") 
            .allowedOriginPatterns("http://localhost:517*") 
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") 
            .allowedHeaders("*") 
            .allowCredentials(true);
    }
}