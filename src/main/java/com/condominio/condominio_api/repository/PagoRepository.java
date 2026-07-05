package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.Pago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    @Query("SELECT p FROM Pago p JOIN FETCH p.cuota c JOIN FETCH p.estado WHERE p.id = :id")
    Optional<Pago> findByIdWithDetails(@Param("id") Long id);

    @Query(value = "SELECT p FROM Pago p JOIN FETCH p.cuota c JOIN FETCH p.estado",
           countQuery = "SELECT count(p) FROM Pago p")
    Page<Pago> findAllWithDetails(Pageable pageable);

    @Query(value = "SELECT p FROM Pago p JOIN FETCH p.cuota c JOIN FETCH p.estado WHERE p.cuota.id = :cuotaId",
           countQuery = "SELECT count(p) FROM Pago p WHERE p.cuota.id = :cuotaId")
    Page<Pago> findByCuotaIdWithDetails(@Param("cuotaId") Long cuotaId, Pageable pageable);
}
