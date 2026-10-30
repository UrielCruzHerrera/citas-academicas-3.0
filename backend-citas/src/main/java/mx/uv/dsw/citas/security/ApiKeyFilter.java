package mx.uv.dsw.citas.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${api.security.enabled:false}")
    private boolean enabled;

    @Value("${api.security.api-key:demo-key-2026}")
    private String apiKeyEsperada;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Si está desactivado, dejamos pasar todo
        if (!enabled) {
            filterChain.doFilter(request, response);
            return;
        }

        // Excluir health y OPTIONS (preflight)
        String path = request.getRequestURI();
        if (path.equals("/api/health") || "OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader("X-API-Key");
        if (!apiKeyEsperada.equals(apiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(
                "{\"error\":\"Unauthorized\",\"message\":\"API Key inválida o ausente\"}"
            );
            return;
        }

        filterChain.doFilter(request, response);
    }
}