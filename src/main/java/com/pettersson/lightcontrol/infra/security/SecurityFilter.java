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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().equals("/login") || request.getRequestURI().equals("/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("Filtro ok");
        //obtener el token de los headers
        var authHeader = request.getHeader("Authorization");
        System.out.println("Authorization" + authHeader);
        if (authHeader != null){
            System.out.println("Token no es null");
            var token = authHeader.replace("Bearer", "").trim();
            System.out.println(token); //hasta acá se ejecuta
            System.out.println("imprimiendo getsubject token" + tokenService.getSubject(token));
            System.out.println("verificando subject");
            var subject = tokenService.getSubject(token);
            if (subject != null){
                //token valido
                System.out.println("subject no es null");
                var usuario = usuarioRepository.findByLogin(subject);
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null,
                        usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        System.out.println("subject si es null");
        filterChain.doFilter(request, response);
    }
}

