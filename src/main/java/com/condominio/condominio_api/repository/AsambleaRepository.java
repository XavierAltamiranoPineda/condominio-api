package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.Asamblea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsambleaRepository extends JpaRepository<Asamblea, Long> {
    Page<Asamblea> findByCondominioId(Long condominioId, Pageable pageable);
}
