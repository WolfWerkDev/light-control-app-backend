package com.pettersson.lightcontrol.controller;

import com.pettersson.lightcontrol.domain.luz.DatosEstadoLuces;
import com.pettersson.lightcontrol.domain.luz.Luz;
import com.pettersson.lightcontrol.domain.producto.*;
import com.pettersson.lightcontrol.domain.usuario.DatosRespuestaUsuario;
import com.pettersson.lightcontrol.domain.usuario.Usuario;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = {"http://localhost:4200", "https://localhost:4200"})
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping("/register")
    public ResponseEntity<?> responseEntity(@RequestBody @Valid DatosRegistroProducto datosRegistroProducto,
                                            UriComponentsBuilder uriComponentsBuilder){
        System.out.println("antes del try de registrar");
        try {
            Producto nuevoProducto = new Producto(datosRegistroProducto);

            Producto producto = productoService.registrarProducto(nuevoProducto);

            DatosProductoRegistrado datosProductoRegistrado = new DatosProductoRegistrado(producto.getId(), producto.getNombreModulo(), producto.getLuces());
            System.out.println("Luces: " + producto.getLuces());

            URI url = uriComponentsBuilder.path("/register-product/{id}").buildAndExpand(producto.getId()).toUri();
            System.out.println("Datos recibidos para registrar producto: " + datosRegistroProducto);

            return ResponseEntity.created(url).body(datosProductoRegistrado);
        } catch (RuntimeException e) {
            e.printStackTrace();  // Esto muestra el error detallado en la consola
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/validation")
    public ResponseEntity<?> responseEntity(@RequestBody @Valid DatosValidarProducto datosValidarProducto,
                                            UriComponentsBuilder uriComponentsBuilder){
        try {
            var producto = productoService.validarProducto(datosValidarProducto.id(), datosValidarProducto.codigoValidacion());

            DatosProductoValidado productoValidado = new DatosProductoValidado(producto.getId(), producto.getNombreModulo(), producto.getCapacidadModulo(), producto.getUsuario().getNombres(), producto.getLuces());
            URI url = uriComponentsBuilder.path("/register-product/validation/{id}").buildAndExpand(producto.getId()).toUri();
            return ResponseEntity.created(url).body(productoValidado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    //Método get para el esp32
    /*
    @GetMapping("/control/{codigoValidacion}")
    public ResponseEntity<DatosProductoValidado> infoControlProduct(@PathVariable String codigoValidacion){
        Producto producto = productoService.infoToEsp32(codigoValidacion);

        var datosProducto = new DatosProductoValidado(producto.getId(), producto.getNombreModulo(), producto.getCapacidadModulo(),producto.getUsuario().getNombres(), producto.getLuces());
        return ResponseEntity.ok().body(datosProducto);
    }
     */

    @GetMapping(value = "/control/{codigoValidacion}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamLightState(@PathVariable String codigoValidacion) {
        return productoService.infoToEsp32(codigoValidacion); // Convertir a JSON antes de enviar
    }



    //Método get para que el cliente vea la lista de productos de X usuario de acuerdo a su id
    @GetMapping("/my-products/{id}")
    public ResponseEntity<List<DatosListaProductosUsuario>> listaProductos(@PathVariable Long id){
        List<DatosListaProductosUsuario> listaDeProductos = productoService.listaProductos(id);

        return ResponseEntity.ok().body(listaDeProductos);
    }

    //Método Put para actualizar los nombres del módulo y sus luces
    @PutMapping("update/{id}")
    @Transactional
    public ResponseEntity<?> responseEntity(@PathVariable Long id, @RequestBody @Valid DatosActualizarNombreProducto datosActualizarNombreProducto,
                                            UriComponentsBuilder uriComponentsBuilder){
        Producto producto = productoService.actualizarInfoDeProducto(id, datosActualizarNombreProducto);

        DatosProductoRegistrado datos = new DatosProductoRegistrado(producto.getId(), producto.getNombreModulo(), producto.getLuces());
        return ResponseEntity.ok().body(datos);
    }
}
