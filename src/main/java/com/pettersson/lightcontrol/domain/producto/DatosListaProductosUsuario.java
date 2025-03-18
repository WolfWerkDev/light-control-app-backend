package com.pettersson.lightcontrol.domain.producto;

import com.pettersson.lightcontrol.domain.luz.Luz;

import java.util.List;

public record DatosListaProductosUsuario(Long id,
                                         int capacidadModulo,
                                         String nombreModulo,
                                         List<Luz> luces) {
}
