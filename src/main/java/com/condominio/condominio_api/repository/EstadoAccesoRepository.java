package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.EstadoAcceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoAccesoRepository extends JpaRepository<EstadoAcceso, Long> {
    Optional<EstadoAcceso> findByNombreIgnoreCase(String nombre);
}
