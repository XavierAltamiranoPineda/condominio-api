package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.PersonaUnidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonaUnidadRepository extends JpaRepository<PersonaUnidad, Long> {

    @Query("SELECT pu FROM PersonaUnidad pu JOIN FETCH pu.persona JOIN FETCH pu.unidad u JOIN FETCH u.condominio WHERE pu.id = :id")
    Optional<PersonaUnidad> findByIdWithDetails(@Param("id") Long id);

    @Query(value = "SELECT pu FROM PersonaUnidad pu JOIN FETCH pu.persona JOIN FETCH pu.unidad u JOIN FETCH u.condominio",
           countQuery = "SELECT count(pu) FROM PersonaUnidad pu")
    Page<PersonaUnidad> findAllWithDetails(Pageable pageable);

    @Query(value = "SELECT pu FROM PersonaUnidad pu JOIN FETCH pu.persona JOIN FETCH pu.unidad u JOIN FETCH u.condominio WHERE pu.persona.id = :personaId",
           countQuery = "SELECT count(pu) FROM PersonaUnidad pu WHERE pu.persona.id = :personaId")
    Page<PersonaUnidad> findByPersonaIdWithDetails(@Param("personaId") Long personaId, Pageable pageable);

    @Query(value = "SELECT pu FROM PersonaUnidad pu JOIN FETCH pu.persona JOIN FETCH pu.unidad u JOIN FETCH u.condominio WHERE pu.unidad.id = :unidadId",
           countQuery = "SELECT count(pu) FROM PersonaUnidad pu WHERE pu.unidad.id = :unidadId")
    Page<PersonaUnidad> findByUnidadIdWithDetails(@Param("unidadId") Long unidadId, Pageable pageable);

    boolean existsByPersonaIdAndUnidadIdAndEstado(Long personaId, Long unidadId, PersonaUnidad.EstadoPersonaUnidad estado);
}
