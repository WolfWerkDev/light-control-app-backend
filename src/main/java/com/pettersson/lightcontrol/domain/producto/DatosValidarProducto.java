package com.pettersson.lightcontrol.domain.producto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosValidarProducto(

        //@NotNull
        Long id,
        //@NotBlank
        String codigoValidacion) {
}
