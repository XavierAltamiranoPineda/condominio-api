package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivoRepository extends JpaRepository<Archivo, Long> {
}
