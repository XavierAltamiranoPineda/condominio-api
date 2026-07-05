package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.Vehiculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    @Query("SELECT v FROM Vehiculo v JOIN FETCH v.unidad u LEFT JOIN FETCH v.personaActual p WHERE v.unidad.id = :unidadId")
    List<Vehiculo> findByUnidadId(@Param("unidadId") Long unidadId);
    
    @Query(value = "SELECT v FROM Vehiculo v JOIN FETCH v.unidad u LEFT JOIN FETCH v.personaActual p WHERE u.condominio.id = :condominioId",
           countQuery = "SELECT COUNT(v) FROM Vehiculo v WHERE v.unidad.condominio.id = :condominioId")
    Page<Vehiculo> findByCondominioId(@Param("condominioId") Long condominioId, Pageable pageable);
}
