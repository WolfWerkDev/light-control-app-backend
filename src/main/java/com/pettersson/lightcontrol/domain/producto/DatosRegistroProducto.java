package com.pettersson.lightcontrol.domain.producto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroProducto(
        @NotNull
        Integer capacidad,
        @NotBlank
        String codigoValidacion
) {
}
