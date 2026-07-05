package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.Cuota;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuotaRepository extends JpaRepository<Cuota, Long> {

    @Query("SELECT c FROM Cuota c JOIN FETCH c.unidad u JOIN FETCH u.condominio WHERE c.id = :id")
    Optional<Cuota> findByIdWithDetails(@Param("id") Long id);

    @Query(value = "SELECT c FROM Cuota c JOIN FETCH c.unidad u JOIN FETCH u.condominio",
           countQuery = "SELECT count(c) FROM Cuota c")
    Page<Cuota> findAllWithDetails(Pageable pageable);

    @Query(value = "SELECT c FROM Cuota c JOIN FETCH c.unidad u JOIN FETCH u.condominio WHERE c.unidad.id = :unidadId",
           countQuery = "SELECT count(c) FROM Cuota c WHERE c.unidad.id = :unidadId")
    Page<Cuota> findByUnidadIdWithDetails(@Param("unidadId") Long unidadId, Pageable pageable);

    boolean existsByUnidadIdAndMesAndAnioAndTipo(Long unidadId, Short mes, Short anio, Cuota.TipoCuota tipo);
    boolean existsByUnidadIdAndMesAndAnioAndTipoAndIdNot(Long unidadId, Short mes, Short anio, Cuota.TipoCuota tipo, Long id);
}
