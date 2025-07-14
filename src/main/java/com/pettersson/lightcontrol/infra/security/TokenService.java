package com.pettersson.lightcontrol.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pettersson.lightcontrol.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.secret}")
    private String apiSecret;

    public String generarToken(Usuario usuario){
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            // System.out.println("Clave secreta utilizada: " + apiSecret);
            return JWT.create()
                    .withIssuer("pettersson")
                    .withSubject(usuario.getLogin())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(generarFechaExpiracion())
                    .sign(algorithm);

        } catch (JWTCreationException exception){
            throw new RuntimeException();
            // Invalid Signing configuration / Couldn't convert Claims.
        }
    }

    public String getSubject(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("El token es nulo o está vacío");
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            // System.out.println("Clave secreta utilizada para subject: " + apiSecret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("pettersson")
                    .build();
            return verifier.verify(token).getSubject();
        } catch (JWTVerificationException exception) {
            // System.out.println("Error al verificar el token: " + exception.getMessage());
            // System.out.println("Token recibido: " + token);
            throw new RuntimeException("El token no es válido", exception);
        }
    }

    private Instant generarFechaExpiracion(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));
    }
    public boolean isValid(String token) {
        try {
            getSubject(token); // Intenta obtener el subject (si falla, el token es inválido)
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
}
