package com.pettersson.lightcontrol.domain.producto;

import com.pettersson.lightcontrol.domain.luz.Luz;
import com.pettersson.lightcontrol.domain.usuario.Usuario;

import java.util.List;

public record DatosProductoValidado(Long id,
                                    String nombreModulo,
                                    int capacidadModulo,
                                    String nombres,
                                    List<Luz> luces) {
}
