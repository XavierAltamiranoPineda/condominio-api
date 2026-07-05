package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.Votacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VotacionRepository extends JpaRepository<Votacion, Long> {

    @Query(value = "SELECT v FROM Votacion v " +
                   "JOIN FETCH v.persona p " +
                   "WHERE v.asamblea.id = :asambleaId",
           countQuery = "SELECT COUNT(v) FROM Votacion v WHERE v.asamblea.id = :asambleaId")
    Page<Votacion> findByAsambleaIdWithDetails(@Param("asambleaId") Long asambleaId, Pageable pageable);

    Optional<Votacion> findByAsambleaIdAndPersonaId(Long asambleaId, Long personaId);

    @Query("SELECT v.opcion, COUNT(v) FROM Votacion v WHERE v.asamblea.id = :asambleaId GROUP BY v.opcion")
    List<Object[]> countVotosByOpcion(@Param("asambleaId") Long asambleaId);
}
