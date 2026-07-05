package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.HistorialTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HistorialTicketRepository extends JpaRepository<HistorialTicket, Long> {

    @Query("SELECT h FROM HistorialTicket h " +
           "JOIN FETCH h.estado e " +
           "JOIN FETCH h.usuario u " +
           "LEFT JOIN FETCH u.persona p " +
           "WHERE h.ticket.id = :ticketId " +
           "ORDER BY h.fecha DESC")
    List<HistorialTicket> findByTicketIdOrderByFechaDesc(Long ticketId);
}
