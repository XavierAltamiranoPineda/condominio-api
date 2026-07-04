package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

    Optional<Persona> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    boolean existsByTipoIdentificacionAndNumeroIdentificacion(
            Persona.TipoIdentificacion tipoIdentificacion,
            String numeroIdentificacion
    );
}
