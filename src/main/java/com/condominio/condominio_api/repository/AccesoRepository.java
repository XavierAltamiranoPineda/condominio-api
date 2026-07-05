package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.Acceso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccesoRepository extends JpaRepository<Acceso, Long> {

    @Query("SELECT a FROM Acceso a " +
           "JOIN FETCH a.visitante v " +
           "JOIN FETCH a.unidad u " +
           "JOIN FETCH a.guardia g " +
           "JOIN FETCH a.estado e " +
           "LEFT JOIN FETCH a.preautorizacion p " +
           "WHERE a.id = :id")
    Optional<Acceso> findByIdWithDetails(@Param("id") Long id);

    @Query(value = "SELECT a FROM Acceso a " +
                   "JOIN FETCH a.visitante v " +
                   "JOIN FETCH a.unidad u " +
                   "JOIN FETCH a.guardia g " +
                   "JOIN FETCH a.estado e " +
                   "LEFT JOIN FETCH a.preautorizacion p " +
                   "WHERE a.unidad.condominio.id = :condominioId",
           countQuery = "SELECT COUNT(a) FROM Acceso a WHERE a.unidad.condominio.id = :condominioId")
    Page<Acceso> findByCondominioIdWithDetails(@Param("condominioId") Long condominioId, Pageable pageable);

    @Query(value = "SELECT a FROM Acceso a " +
                   "JOIN FETCH a.visitante v " +
                   "JOIN FETCH a.unidad u " +
                   "JOIN FETCH a.guardia g " +
                   "JOIN FETCH a.estado e " +
                   "LEFT JOIN FETCH a.preautorizacion p " +
                   "WHERE a.unidad.id = :unidadId",
           countQuery = "SELECT COUNT(a) FROM Acceso a WHERE a.unidad.id = :unidadId")
    Page<Acceso> findByUnidadIdWithDetails(@Param("unidadId") Long unidadId, Pageable pageable);
}
