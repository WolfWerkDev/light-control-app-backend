package com.pettersson.lightcontrol.domain.producto;

import com.pettersson.lightcontrol.domain.luz.DatosEstadoLuces;
import com.pettersson.lightcontrol.domain.luz.Luz;
import com.pettersson.lightcontrol.domain.luz.LuzRepository;
import com.pettersson.lightcontrol.domain.luz.LuzService;
import com.pettersson.lightcontrol.domain.usuario.Usuario;
import com.pettersson.lightcontrol.domain.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final Sinks.Many<List<Luz>> luzSink = Sinks.many().replay().latest();

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private LuzRepository luzRepository;
    @Autowired
    private LuzService luzService;

    //Para verificar que existe el producto según el codigo 9 dígitos
    public boolean productoExiste(String codigoValidacion) {
        return productoRepository.findBycodigoValidacion(codigoValidacion).isPresent();
    }

    public Producto registrarProducto(Producto producto){

        //Asignar la capacidad del módulo from DTO
        int capacidad = producto.getCapacidadModulo();
        producto.setCapacidadModulo(capacidad);
        //Asignar el código de verificación que viene del DTO
        String codigo = producto.getCodigoValidacion();
        producto.setCodigoValidacion(codigo);
        //Asignar caracteristicas por defecto
        producto.setNombreModulo("Mí módulo");

        Producto productoGuardado = productoRepository.save(producto);

        registroInfoLuces(productoGuardado);
        //System.out.println("producto registrado con éxito");
        return productoGuardado;
    }

    public Producto validarProducto(Long id, String codigoValidacion) {
        // Buscar el producto en la base de datos según el código de validación
        Producto producto = productoRepository.findBycodigoValidacion(codigoValidacion)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Verificar si el producto ya tiene un usuario asignado
        if (producto.getUsuario() != null) {
            throw new RuntimeException("El producto ya está registrado por otro usuario");
        }

        // Buscar el usuario en la base de datos según el ID
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Asignar el usuario al producto
        producto.setUsuario(usuario);

        // Guardar el producto actualizado en la base de datos
        //System.out.println("producto asignado al usuario: " + usuario);
        return productoRepository.save(producto);

    }
    public void registroInfoLuces(Producto producto){
        //System.out.println("Registrando luces");
        int capacidad = producto.getCapacidadModulo();
        for (int i = 0; i < capacidad; i++) {
            Luz luz = new Luz();
            luz.setNombreLuz("Luminaria #" + (i+1));
            luz.setProducto(producto);
            luz.setNumeroLuz(i+1);
            luz.setEstadoLuz(false);

            luzRepository.save(luz);
        }
    }

    public Flux<String> infoToEsp32(String codigoValidacion) {
        Producto producto = productoRepository.findBycodigoValidacion(codigoValidacion)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Long idModulo = producto.getId();

        return luzService.getLuzStream(idModulo)
                .map(DatosEstadoLuces::new)
                .map(this::convertirAJson) // Convertir a JSON antes de enviarlo
                .doOnNext(json -> System.out.println("To ESP32: " + json));
    }

    private String convertirAJson(DatosEstadoLuces datos) {
        StringBuilder json = new StringBuilder();
        json.append("{\"luces\":[");

        List<Luz> luces = datos.luces();
        if (luces == null || luces.isEmpty()) {
            json.append("]}");
            return json.toString();
        }

        for (int i = 0; i < luces.size(); i++) {
            Luz luz = luces.get(i);
            json.append("{")
                    .append("\"id\":").append(luz.getId()).append(",")
                    .append("\"estado\":").append(luz.isEstadoLuz()).append(",")
                    .append("\"nombreLuz\":\"").append(luz.getNombreLuz()).append("\",")
                    .append("\"numeroLuz\":").append(luz.getNumeroLuz())
                    .append("}");
            if (i < luces.size() - 1) {
                json.append(",");
            }
        }
        json.append("]}");
        return json.toString();
    }



    public List<DatosListaProductosUsuario> listaProductos(Long id) {
        List<Producto> listaDeProductos = productoRepository.findByUsuario_Id(id);

        return listaDeProductos.stream()
                .sorted(Comparator.comparingLong(Producto::getId)) // Ordenar productos por ID
                .map(p -> new DatosListaProductosUsuario(
                        p.getId(),
                        p.getCapacidadModulo(),
                        p.getNombreModulo(),
                        p.getLuces().stream()
                                .sorted(Comparator.comparingInt(Luz::getNumeroLuz)) // Ordenar luces dentro del producto
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }


    @Transactional
    public Producto actualizarInfoDeProducto(Long id, DatosActualizarNombreProducto datos){
        //System.out.println("Nombre recibido: " + datos.nombreModulo());
        Producto producto = productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        //System.out.println("Luces in: " + datos.luces().toString());
        List<Luz> luces = datos.luces();
        if (luces != null) {
            for (Luz luz : luces) {
                Long idLuz = luz.getId();
                String nombreLuz = luz.getNombreLuz();
                //System.out.println("ID: " + idLuz + ", Nombre: " + nombreLuz);
                Luz luzActual = luzRepository.findById(idLuz).orElseThrow(() -> new RuntimeException("luz no encontrada"));
                luzActual.setNombreLuz(nombreLuz);
                luzRepository.save(luzActual);
            }
        } else {
           // System.out.println("No hay luces registradas.");
            throw new RuntimeException("Error al actualizar la información");
        }
        producto.setNombreModulo(datos.nombreModulo());
        return productoRepository.save(producto);
    }
}
