package com.pettersson.lightcontrol.domain.usuario;

import com.pettersson.lightcontrol.infra.security.TokenService;

public record DatosRespuestaLogin(String tokenService,
                                  Long id,
                                  String nombres,
                                  String apellidos,
                                  String email) {
}
