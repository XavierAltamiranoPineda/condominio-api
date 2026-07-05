package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.TicketComentario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TicketComentarioRepository extends JpaRepository<TicketComentario, Long> {

    @Query(value = "SELECT c FROM TicketComentario c " +
                   "JOIN FETCH c.persona p " +
                   "WHERE c.ticket.id = :ticketId",
           countQuery = "SELECT COUNT(c) FROM TicketComentario c WHERE c.ticket.id = :ticketId")
    Page<TicketComentario> findByTicketIdWithDetails(Long ticketId, Pageable pageable);
}
