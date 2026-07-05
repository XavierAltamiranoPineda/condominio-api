package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.ConvenioPago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConvenioPagoRepository extends JpaRepository<ConvenioPago, Long> {

    @Query("SELECT c FROM ConvenioPago c JOIN FETCH c.unidad u JOIN FETCH c.persona p WHERE c.id = :id")
    Optional<ConvenioPago> findByIdWithDetails(@Param("id") Long id);

    @Query(value = "SELECT c FROM ConvenioPago c JOIN FETCH c.unidad u JOIN FETCH c.persona p WHERE u.condominio.id = :condominioId",
           countQuery = "SELECT COUNT(c) FROM ConvenioPago c WHERE c.unidad.condominio.id = :condominioId")
    Page<ConvenioPago> findByCondominioIdWithDetails(@Param("condominioId") Long condominioId, Pageable pageable);

    @Query(value = "SELECT c FROM ConvenioPago c JOIN FETCH c.unidad u JOIN FETCH c.persona p WHERE c.persona.id = :personaId",
           countQuery = "SELECT COUNT(c) FROM ConvenioPago c WHERE c.persona.id = :personaId")
    Page<ConvenioPago> findByPersonaIdWithDetails(@Param("personaId") Long personaId, Pageable pageable);
}
