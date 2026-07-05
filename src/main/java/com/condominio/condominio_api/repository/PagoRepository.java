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

    @Query("SELECT COALESCE(SUM(p.valor), 0) FROM Pago p WHERE p.cuota.id = :cuotaId AND p.estado.nombre = 'CONFIRMADO'")
    java.math.BigDecimal sumPagosConfirmadosByCuotaId(@Param("cuotaId") Long cuotaId);

    @Query(value = "SELECT COALESCE(SUM(p.valor), 0) FROM pago p " +
                   "JOIN cuota c ON p.id_cuota = c.id_cuota " +
                   "JOIN unidad u ON c.id_unidad = u.id_unidad " +
                   "JOIN estado_pago ep ON p.id_estado = ep.id_estado " +
                   "WHERE u.id_condominio = :condominioId " +
                   "  AND ep.nombre = 'CONFIRMADO' " +
                   "  AND EXTRACT(MONTH FROM p.fecha) = :mes " +
                   "  AND EXTRACT(YEAR FROM p.fecha) = :anio", nativeQuery = true)
    java.math.BigDecimal sumRecaudacionMensual(@Param("condominioId") Long condominioId, 
                                               @Param("mes") int mes, 
                                               @Param("anio") int anio);

    @Query(value = "SELECT CAST(EXTRACT(YEAR FROM p.fecha) AS INTEGER) as anio, " +
                   "       CAST(EXTRACT(MONTH FROM p.fecha) AS INTEGER) as mes, " +
                   "       SUM(p.valor) as total " +
                   "FROM pago p " +
                   "JOIN cuota c ON p.id_cuota = c.id_cuota " +
                   "JOIN unidad u ON c.id_unidad = u.id_unidad " +
                   "JOIN estado_pago ep ON p.id_estado = ep.id_estado " +
                   "WHERE u.id_condominio = :condominioId " +
                   "  AND ep.nombre = 'CONFIRMADO' " +
                   "  AND p.fecha >= (CURRENT_DATE - INTERVAL '6 months') " +
                   "GROUP BY CAST(EXTRACT(YEAR FROM p.fecha) AS INTEGER), CAST(EXTRACT(MONTH FROM p.fecha) AS INTEGER) " +
                   "ORDER BY anio ASC, mes ASC", nativeQuery = true)
    java.util.List<com.condominio.condominio_api.repository.projection.RecaudacionMensualProjection> getRecaudacionUltimos6Meses(@Param("condominioId") Long condominioId);
}
