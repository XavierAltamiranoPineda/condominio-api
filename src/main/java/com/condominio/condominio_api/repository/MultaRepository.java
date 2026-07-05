package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.Multa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MultaRepository extends JpaRepository<Multa, Long> {

    @Query("SELECT m FROM Multa m JOIN FETCH m.unidad u JOIN FETCH m.persona p LEFT JOIN FETCH m.cuota c WHERE m.id = :id")
    Optional<Multa> findByIdWithDetails(@Param("id") Long id);

    @Query(value = "SELECT m FROM Multa m JOIN FETCH m.unidad u JOIN FETCH m.persona p LEFT JOIN FETCH m.cuota c WHERE u.condominio.id = :condominioId",
           countQuery = "SELECT COUNT(m) FROM Multa m WHERE m.unidad.condominio.id = :condominioId")
    Page<Multa> findByCondominioIdWithDetails(@Param("condominioId") Long condominioId, Pageable pageable);

    @Query(value = "SELECT m FROM Multa m JOIN FETCH m.unidad u JOIN FETCH m.persona p LEFT JOIN FETCH m.cuota c WHERE m.persona.id = :personaId",
           countQuery = "SELECT COUNT(m) FROM Multa m WHERE m.persona.id = :personaId")
    Page<Multa> findByPersonaIdWithDetails(@Param("personaId") Long personaId, Pageable pageable);
}
