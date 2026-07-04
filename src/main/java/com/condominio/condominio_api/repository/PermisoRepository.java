package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PermisoRepository extends JpaRepository<Permiso, Long> {

    boolean existsByModuloAndAccion(String modulo, String accion);

    Optional<Permiso> findByNombre(String nombre);

    List<Permiso> findByModulo(String modulo);

    @Query("SELECT p FROM Permiso p ORDER BY p.modulo, p.accion")
    List<Permiso> findAllOrdered();
}
