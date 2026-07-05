package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.AreaComun;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaComunRepository extends JpaRepository<AreaComun, Long> {
    Page<AreaComun> findByCondominioId(Long condominioId, Pageable pageable);
}
