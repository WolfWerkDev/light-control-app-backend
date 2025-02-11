package com.pettersson.lightcontrol.domain.producto;

import com.pettersson.lightcontrol.domain.usuario.Usuario;
import com.pettersson.lightcontrol.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

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

        System.out.println("producto registrado con éxito");
        return productoRepository.save(producto);
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
        System.out.println("producto asignado al usuario: " + usuario);
        return productoRepository.save(producto);

    }


}
