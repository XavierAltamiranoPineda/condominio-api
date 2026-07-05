package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.Acta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActaRepository extends JpaRepository<Acta, Long> {
    
    @Query("SELECT a FROM Acta a LEFT JOIN FETCH a.archivos WHERE a.asamblea.id = :asambleaId")
    Optional<Acta> findByAsambleaIdWithArchivos(@Param("asambleaId") Long asambleaId);
}
