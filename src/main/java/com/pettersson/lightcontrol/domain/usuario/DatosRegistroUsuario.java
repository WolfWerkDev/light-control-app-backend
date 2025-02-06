package com.pettersson.lightcontrol.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DatosRegistroUsuario(@NotBlank
                                   String nombres,
                                   @NotBlank
                                   String apellidos,
                                   @NotBlank
                                   @Email
                                   String email,
                                   @NotBlank
                                   String password) {
}
