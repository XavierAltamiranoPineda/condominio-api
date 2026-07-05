package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.EstadoTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoTicketRepository extends JpaRepository<EstadoTicket, Long> {
    Optional<EstadoTicket> findByNombreIgnoreCase(String nombre);
}
