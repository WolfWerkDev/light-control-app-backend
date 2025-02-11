package com.pettersson.lightcontrol.controller;

import com.pettersson.lightcontrol.domain.producto.*;
import com.pettersson.lightcontrol.domain.usuario.DatosRespuestaUsuario;
import com.pettersson.lightcontrol.domain.usuario.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

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

            DatosProductoRegistrado datosProductoRegistrado = new DatosProductoRegistrado(producto.getId(), producto.getNombreModulo());

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
}
