package com.pettersson.lightcontrol.domain.luz;

import com.pettersson.lightcontrol.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LuzRepository extends JpaRepository<Luz, Long> {
}
