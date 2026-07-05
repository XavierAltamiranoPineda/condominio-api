package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.Parqueadero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParqueaderoRepository extends JpaRepository<Parqueadero, Long> {

    @Query("SELECT p FROM Parqueadero p JOIN FETCH p.unidad u WHERE p.unidad.id = :unidadId")
    List<Parqueadero> findByUnidadId(@Param("unidadId") Long unidadId);
    
    @Query(value = "SELECT p FROM Parqueadero p JOIN FETCH p.unidad u WHERE u.condominio.id = :condominioId",
           countQuery = "SELECT COUNT(p) FROM Parqueadero p WHERE p.unidad.condominio.id = :condominioId")
    Page<Parqueadero> findByCondominioId(@Param("condominioId") Long condominioId, Pageable pageable);
}
