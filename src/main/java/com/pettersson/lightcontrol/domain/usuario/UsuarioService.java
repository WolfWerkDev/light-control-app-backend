package com.pettersson.lightcontrol.domain.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Verifica si el usuario ya existe por email
    public boolean usuarioExiste(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    // Registra un nuevo usuario, encriptando la contraseña antes de guardarla
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioExiste(usuario.getEmail())) {
            throw new RuntimeException("El usuario ya está registrado");
        }

        // Encriptar la contraseña antes de guardarla
        String contrasenaEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(contrasenaEncriptada);

        // Guardar el usuario en la base de datos
        return usuarioRepository.save(usuario);
    }
}
