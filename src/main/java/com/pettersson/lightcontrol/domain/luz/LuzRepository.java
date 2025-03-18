package com.pettersson.lightcontrol.domain.luz;

import com.pettersson.lightcontrol.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LuzRepository extends JpaRepository<Luz, Long> {
    List<Luz> findByProducto_Id(Long id);
}
