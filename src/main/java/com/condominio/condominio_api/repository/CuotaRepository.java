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

    @Query("SELECT COUNT(c) FROM Cuota c WHERE c.unidad.condominio.id = :condominioId AND c.fechaVencimiento < CURRENT_DATE AND c.estado IN ('PENDIENTE', 'PAGADA_PARCIAL')")
    long countCuotasVencidasByCondominioId(@Param("condominioId") Long condominioId);

    @Query(value = "SELECT COALESCE(SUM(c.valor - COALESCE(p_sum.pagado, 0)), 0) " +
           "FROM cuota c " +
           "JOIN unidad u ON c.id_unidad = u.id_unidad " +
           "LEFT JOIN ( " +
           "    SELECT p.id_cuota, SUM(p.valor) as pagado " +
           "    FROM pago p " +
           "    JOIN estado_pago ep ON p.id_estado = ep.id_estado " +
           "    WHERE ep.nombre = 'CONFIRMADO' " +
           "    GROUP BY p.id_cuota " +
           ") p_sum ON c.id_cuota = p_sum.id_cuota " +
           "WHERE u.id_condominio = :condominioId " +
           "  AND c.estado IN ('PENDIENTE', 'PAGADA_PARCIAL')", nativeQuery = true)
    java.math.BigDecimal sumSaldoPendienteTotalByCondominioId(@Param("condominioId") Long condominioId);

    @Query(value = "SELECT COALESCE(SUM(c.valor - COALESCE(p_sum.pagado, 0)), 0) " +
           "FROM cuota c " +
           "JOIN unidad u ON c.id_unidad = u.id_unidad " +
           "LEFT JOIN ( " +
           "    SELECT p.id_cuota, SUM(p.valor) as pagado " +
           "    FROM pago p " +
           "    JOIN estado_pago ep ON p.id_estado = ep.id_estado " +
           "    WHERE ep.nombre = 'CONFIRMADO' " +
           "    GROUP BY p.id_cuota " +
           ") p_sum ON c.id_cuota = p_sum.id_cuota " +
           "WHERE u.id_condominio = :condominioId " +
           "  AND c.fecha_vencimiento < CURRENT_DATE " +
           "  AND c.estado IN ('PENDIENTE', 'PAGADA_PARCIAL')", nativeQuery = true)
    java.math.BigDecimal sumSaldoVencidoTotalByCondominioId(@Param("condominioId") Long condominioId);
}
