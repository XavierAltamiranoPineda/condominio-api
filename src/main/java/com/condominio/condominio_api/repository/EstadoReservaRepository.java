package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoReservaRepository extends JpaRepository<EstadoReserva, Long> {
    Optional<EstadoReserva> findByNombreIgnoreCase(String nombre);
}
