package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.Torre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TorreRepository extends JpaRepository<Torre, Long> {

    @Query("SELECT t FROM Torre t JOIN FETCH t.condominio WHERE t.id = :id")
    Optional<Torre> findByIdWithCondominio(@Param("id") Long id);

    @Query(value = "SELECT t FROM Torre t JOIN FETCH t.condominio",
           countQuery = "SELECT count(t) FROM Torre t")
    Page<Torre> findAllWithCondominio(Pageable pageable);

    @Query(value = "SELECT t FROM Torre t JOIN FETCH t.condominio WHERE t.condominio.id = :condominioId",
           countQuery = "SELECT count(t) FROM Torre t WHERE t.condominio.id = :condominioId")
    Page<Torre> findByCondominioIdWithCondominio(@Param("condominioId") Long condominioId, Pageable pageable);

    boolean existsByNombreIgnoreCaseAndCondominioId(String nombre, Long condominioId);
    boolean existsByNombreIgnoreCaseAndCondominioIdAndIdNot(String nombre, Long condominioId, Long id);
}
