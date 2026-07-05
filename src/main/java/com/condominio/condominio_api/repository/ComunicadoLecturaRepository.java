package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.ComunicadoLectura;
import com.condominio.condominio_api.entity.ComunicadoLecturaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ComunicadoLecturaRepository extends JpaRepository<ComunicadoLectura, ComunicadoLecturaId> {
    @Query("SELECT COUNT(c) > 0 FROM ComunicadoLectura c WHERE c.id.idComunicado = :comunicadoId AND c.id.idPersona = :personaId")
    boolean existsByIdComunicadoAndIdPersona(@org.springframework.data.repository.query.Param("comunicadoId") Long comunicadoId, @org.springframework.data.repository.query.Param("personaId") Long personaId);
}
