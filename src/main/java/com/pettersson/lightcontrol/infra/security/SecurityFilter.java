package com.pettersson.lightcontrol.infra.security;

import com.pettersson.lightcontrol.domain.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Permitir acceso sin autenticación solo a /login y /register
        if (request.getRequestURI().equals("/login") || request.getRequestURI().equals("/register") || request.getRequestURI().equals("/product/control/{codigoValidacion}")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Obtener el token del header Authorization
        var authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            var token = authHeader.replace("Bearer ", "").trim();
            var subject = tokenService.getSubject(token);

            if (subject != null) {
                var usuario = usuarioRepository.findByLogin(subject);

                if (usuario != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(
                            usuario, null, usuario.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        // Continuar con la ejecución de la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
