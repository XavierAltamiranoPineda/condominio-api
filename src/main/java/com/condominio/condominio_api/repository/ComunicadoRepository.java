package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.Comunicado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComunicadoRepository extends JpaRepository<Comunicado, Long> {

    @Query("SELECT c FROM Comunicado c JOIN FETCH c.autor a WHERE c.id = :id")
    Optional<Comunicado> findByIdWithAutor(@Param("id") Long id);

    @Query(value = "SELECT c FROM Comunicado c JOIN FETCH c.autor a",
           countQuery = "SELECT COUNT(c) FROM Comunicado c")
    Page<Comunicado> findAllWithAutor(Pageable pageable);
    
    // Aquí podríamos añadir queries para filtrar por destinatario_tipo y destinatario_id
    // para que un residente solo vea los comunicados de su torre/unidad o generales
}
