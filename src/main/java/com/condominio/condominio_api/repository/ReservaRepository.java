package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("SELECT r FROM Reserva r " +
           "JOIN FETCH r.area a " +
           "JOIN FETCH r.persona p " +
           "JOIN FETCH r.estado e " +
           "LEFT JOIN FETCH r.usuarioAprobador ua " +
           "WHERE r.id = :id")
    Optional<Reserva> findByIdWithDetails(@Param("id") Long id);

    @Query(value = "SELECT r FROM Reserva r " +
                   "JOIN FETCH r.area a " +
                   "JOIN FETCH r.persona p " +
                   "JOIN FETCH r.estado e " +
                   "LEFT JOIN FETCH r.usuarioAprobador ua " +
                   "WHERE r.area.condominio.id = :condominioId",
           countQuery = "SELECT COUNT(r) FROM Reserva r WHERE r.area.condominio.id = :condominioId")
    Page<Reserva> findByCondominioIdWithDetails(@Param("condominioId") Long condominioId, Pageable pageable);

    @Query(value = "SELECT r FROM Reserva r " +
                   "JOIN FETCH r.area a " +
                   "JOIN FETCH r.persona p " +
                   "JOIN FETCH r.estado e " +
                   "LEFT JOIN FETCH r.usuarioAprobador ua " +
                   "WHERE r.area.id = :areaId",
           countQuery = "SELECT COUNT(r) FROM Reserva r WHERE r.area.id = :areaId")
    Page<Reserva> findByAreaIdWithDetails(@Param("areaId") Long areaId, Pageable pageable);

    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.area.id = :areaId " +
           "AND r.fecha = :fecha " +
           "AND r.estado.nombre NOT IN ('RECHAZADA', 'CANCELADA') " +
           "AND r.bloqueaHorario = true " +
           "AND (r.horaInicio < :horaFin AND r.horaFin > :horaInicio)")
    long countSuperposiciones(@Param("areaId") Long areaId,
                              @Param("fecha") LocalDate fecha,
                              @Param("horaInicio") LocalTime horaInicio,
                              @Param("horaFin") LocalTime horaFin);
                              
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.area.id = :areaId " +
           "AND r.fecha = :fecha " +
           "AND r.id != :reservaId " +
           "AND r.estado.nombre NOT IN ('RECHAZADA', 'CANCELADA') " +
           "AND r.bloqueaHorario = true " +
           "AND (r.horaInicio < :horaFin AND r.horaFin > :horaInicio)")
    long countSuperposicionesExcluyendo(@Param("areaId") Long areaId,
                                        @Param("fecha") LocalDate fecha,
                                        @Param("horaInicio") LocalTime horaInicio,
                                        @Param("horaFin") LocalTime horaFin,
                                        @Param("reservaId") Long reservaId);
}
