package br.com.tarefas.api.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Filter explícito para responder pré-flights OPTIONS com cabeçalhos CORS
 * durante o desenvolvimento. Isso contorna possíveis pontos em que o
 * processamento CORS padrão falha antes de escrever os cabeçalhos.
 * NOTA: Use apenas em desenvolvimento.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PreflightCorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String origin = req.getHeader("Origin");
        if (origin != null) {
            // Ajuste permissivo para desenvolvimento
            res.setHeader("Access-Control-Allow-Origin", origin.equals("null") ? "*" : origin);
            res.setHeader("Vary", "Origin");
            res.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            res.setHeader("Access-Control-Allow-Headers", "Origin,Content-Type,Accept,Authorization");
            res.setHeader("Access-Control-Allow-Credentials", "false");
        }

        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            // Responder pré-flight imediatamente
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }
}
