package com.pettersson.lightcontrol.domain.producto;

import com.pettersson.lightcontrol.domain.luz.Luz;

import java.util.List;

public record DatosProductoRegistrado(
        Long id,
        String nombreModulo,
        List<Luz> luces) { //Agregar List<Luz> luces para enviar también la info de luces cuando se registra
}
