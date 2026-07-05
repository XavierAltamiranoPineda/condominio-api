package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.Recibo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReciboRepository extends JpaRepository<Recibo, Long> {

    @Query("SELECT r FROM Recibo r JOIN FETCH r.pago p LEFT JOIN FETCH r.archivo WHERE r.id = :id")
    Optional<Recibo> findByIdWithDetails(@Param("id") Long id);

    @Query(value = "SELECT r FROM Recibo r JOIN FETCH r.pago p LEFT JOIN FETCH r.archivo",
           countQuery = "SELECT count(r) FROM Recibo r")
    Page<Recibo> findAllWithDetails(Pageable pageable);

    @Query(value = "SELECT r FROM Recibo r JOIN FETCH r.pago p LEFT JOIN FETCH r.archivo WHERE r.pago.id = :pagoId",
           countQuery = "SELECT count(r) FROM Recibo r WHERE r.pago.id = :pagoId")
    Page<Recibo> findByPagoIdWithDetails(@Param("pagoId") Long pagoId, Pageable pageable);

    boolean existsByNumeroIgnoreCase(String numero);
    boolean existsByNumeroIgnoreCaseAndIdNot(String numero, Long id);
    
    boolean existsByPagoId(Long pagoId);
    boolean existsByPagoIdAndIdNot(Long pagoId, Long id);
}
