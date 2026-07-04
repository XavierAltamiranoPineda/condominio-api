package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.EstadoUnidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoUnidadRepository extends JpaRepository<EstadoUnidad, Long> {
    Optional<EstadoUnidad> findByNombreIgnoreCase(String nombre);
}
