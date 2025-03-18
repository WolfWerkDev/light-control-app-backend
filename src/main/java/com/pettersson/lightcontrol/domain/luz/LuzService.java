package com.pettersson.lightcontrol.domain.luz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LuzService {

    private final Sinks.Many<List<Luz>> luzSink = Sinks.many().replay().latest();

    @Autowired
    private LuzRepository luzRepository;

    public List<Luz> obtenerEstadoLuces(Long id) {
        return luzRepository.findByProducto_Id(id)
                .stream()
                .sorted(Comparator.comparingInt(Luz::getNumeroLuz)) // Orden ascendente
                .collect(Collectors.toList());
    }

    @Transactional
    public Luz actualizarEstadoLuz(Long id, boolean estado) {
        Luz luz = luzRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Luz no encontrada"));

        luz.setEstadoLuz(estado);
        luzRepository.save(luz);

        // Emitir evento cuando haya un cambio en la base de datos
        luzSink.tryEmitNext(obtenerEstadoLuces(luz.getProducto().getId()));
        System.out.println("Emitiendo evento");

        return luz;
    }

    public Flux<List<Luz>> getLuzStream(Long id) {
        return luzSink.asFlux()
                .filter(luces -> !luces.isEmpty() && luces.stream().anyMatch(luz -> luz.getProducto().getId().equals(id)));
    }

}
