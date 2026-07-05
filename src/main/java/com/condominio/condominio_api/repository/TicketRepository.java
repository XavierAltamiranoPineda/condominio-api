package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t " +
           "JOIN FETCH t.persona p " +
           "JOIN FETCH t.unidad u " +
           "LEFT JOIN FETCH t.tecnico te " +
           "LEFT JOIN FETCH t.categoria c " +
           "LEFT JOIN FETCH t.estadoActual e " +
           "WHERE t.id = :id")
    Optional<Ticket> findByIdWithDetails(Long id);

    @Query(value = "SELECT t FROM Ticket t " +
                   "JOIN FETCH t.persona p " +
                   "JOIN FETCH t.unidad u " +
                   "LEFT JOIN FETCH t.tecnico te " +
                   "LEFT JOIN FETCH t.categoria c " +
                   "LEFT JOIN FETCH t.estadoActual e",
           countQuery = "SELECT COUNT(t) FROM Ticket t")
    Page<Ticket> findAllWithDetails(Pageable pageable);

    @Query(value = "SELECT t FROM Ticket t " +
                   "JOIN FETCH t.persona p " +
                   "JOIN FETCH t.unidad u " +
                   "LEFT JOIN FETCH t.tecnico te " +
                   "LEFT JOIN FETCH t.categoria c " +
                   "LEFT JOIN FETCH t.estadoActual e " +
                   "WHERE t.unidad.condominio.id = :condominioId",
           countQuery = "SELECT COUNT(t) FROM Ticket t WHERE t.unidad.condominio.id = :condominioId")
    Page<Ticket> findByCondominioIdWithDetails(Long condominioId, Pageable pageable);
}
