package com.pettersson.lightcontrol.domain.producto;

import com.pettersson.lightcontrol.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findBycodigoValidacion(String codigoValidacion);
    List<Producto> findByUsuario_Id(Long id);
}
