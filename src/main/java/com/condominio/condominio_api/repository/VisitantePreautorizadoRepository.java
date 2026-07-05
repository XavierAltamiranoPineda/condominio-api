package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.VisitantePreautorizado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitantePreautorizadoRepository extends JpaRepository<VisitantePreautorizado, Long> {

    @Query(value = "SELECT v FROM VisitantePreautorizado v " +
                   "JOIN FETCH v.visitante vi " +
                   "JOIN FETCH v.unidad u " +
                   "JOIN FETCH v.autorizadoPor p " +
                   "WHERE v.unidad.condominio.id = :condominioId",
           countQuery = "SELECT COUNT(v) FROM VisitantePreautorizado v WHERE v.unidad.condominio.id = :condominioId")
    Page<VisitantePreautorizado> findByCondominioIdWithDetails(@Param("condominioId") Long condominioId, Pageable pageable);

    @Query(value = "SELECT v FROM VisitantePreautorizado v " +
                   "JOIN FETCH v.visitante vi " +
                   "JOIN FETCH v.unidad u " +
                   "JOIN FETCH v.autorizadoPor p " +
                   "WHERE v.unidad.id = :unidadId",
           countQuery = "SELECT COUNT(v) FROM VisitantePreautorizado v WHERE v.unidad.id = :unidadId")
    Page<VisitantePreautorizado> findByUnidadIdWithDetails(@Param("unidadId") Long unidadId, Pageable pageable);
}
