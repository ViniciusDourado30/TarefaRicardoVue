package br.com.tarefas.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * CorsFilter registrado como Bean para garantir que pré-flights (OPTIONS)
 * e requisições CORS sejam tratadas cedo na cadeia de filtros.
 * Esta configuração é adequada para desenvolvimento local.
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // Em desenvolvimento permitimos origens curinga para evitar bloqueios de preflight.
        // NOTA: não use em produção. Se precisar de credenciais (cookies), ajuste com origens específicas.
        config.setAllowCredentials(false);
        config.setAllowedOrigins(List.of("*"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
