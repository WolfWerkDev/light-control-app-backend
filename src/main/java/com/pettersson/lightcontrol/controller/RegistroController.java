package com.pettersson.lightcontrol.controller;

import com.pettersson.lightcontrol.domain.usuario.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/register")
@CrossOrigin(origins = {"http://localhost:4200", "https://localhost:4200"})
public class RegistroController {

    @Autowired
    private UsuarioService usuarioService;

    // Método para registrar el usuario (POST)
    @PostMapping
    public ResponseEntity<?> registrarUsuario(@RequestBody @Valid DatosRegistroUsuario datosRegistroUsuario,
                                              UriComponentsBuilder uriComponentsBuilder) {
        try {
            // Aquí no es necesario encriptar la contraseña, se hace en el servicio
            Usuario nuevoUsuario = new Usuario(datosRegistroUsuario);

            // Registrar el usuario, la contraseña ya es encriptada en el servicio
            Usuario usuario = usuarioService.registrarUsuario(nuevoUsuario);

            DatosRespuestaUsuario datosRespuestaUsuario = new DatosRespuestaUsuario(
                    usuario.getId(), usuario.getNombres(), usuario.getApellidos(), usuario.getEmail()
            );

            URI url = uriComponentsBuilder.path("/register/{id}").buildAndExpand(usuario.getId()).toUri();
            return ResponseEntity.created(url).body(datosRespuestaUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Método para manejar solicitudes OPTIONS (CORS preflight)
    @RequestMapping(value = "/register", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> options() {
        return ResponseEntity.ok().build();
    }
}
