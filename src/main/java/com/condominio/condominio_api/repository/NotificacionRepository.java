package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.Notificacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    
    @Query("SELECT n FROM Notificacion n JOIN FETCH n.persona WHERE n.persona.id = :personaId")
    Page<Notificacion> findByPersonaId(@Param("personaId") Long personaId, Pageable pageable);
}
