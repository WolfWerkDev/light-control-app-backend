package com.pettersson.lightcontrol.controller;

import com.pettersson.lightcontrol.domain.luz.*;
import com.pettersson.lightcontrol.infra.security.TokenService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/control")
public class LuzController {

    @Autowired
    private LuzService luzService;
    @Autowired
    private TokenService tokenService;

    //Método put para actualizar el estado de las luces desde el front
    @PutMapping("/state")
    @Transactional
    public ResponseEntity<DatosEstadoLuces> responseEntity(@RequestBody @Valid CambiarEstadoLuces cambiarEstadoLuces) {
        Luz luz = luzService.actualizarEstadoLuz(cambiarEstadoLuces.id(), cambiarEstadoLuces.estado());
        DatosEstadoLuces datosRespuesta = new DatosEstadoLuces(luz.getProducto().getLuces());

        return ResponseEntity.ok().body(datosRespuesta);
    }


    //Método para devolver el estado de las luces al front

    @GetMapping("/lights/{id}")
    public ResponseEntity<?> responseEntity(@PathVariable Long id){
        List<Luz> luz = luzService.obtenerEstadoLuces(id);

        return ResponseEntity.ok().body(luz);
    }
    //Endpoint SSE para actualización de estado de luces en tiempo real
    @GetMapping(value = "/{id}/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<Luz>> streamLightState(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        if (!tokenService.isValid(token.replace("Bearer ", ""))) {
            return Flux.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido"));
        }

        return Flux.merge(
                luzService.getLuzStream(id), // Envía datos iniciales
                Flux.interval(Duration.ofSeconds(30))
                        .flatMap(tick -> luzService.getLuzStream(id)), // Envía actualizaciones cada 30s
                Flux.interval(Duration.ofSeconds(15))
                        .map(tick -> List.of()) // Keep-alive enviando una lista vacía
        );
    }
}
