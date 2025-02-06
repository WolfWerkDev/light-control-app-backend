package com.pettersson.lightcontrol.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CorsController {

    // Asegúrate de manejar las solicitudes OPTIONS correctamente
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public void handleOptionsRequest() {
        // Nada que devolver, solo asegurarse de que se maneja la solicitud preflight
    }
}
