package com.pettersson.lightcontrol.domain.producto;

import com.pettersson.lightcontrol.domain.luz.Luz;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DatosActualizarNombreProducto(@NotNull
                                            String nombreModulo,
                                            List<Luz> luces) {
}
