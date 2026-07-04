package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.Rol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    Optional<Rol> findByNombre(String nombre);

    /** Para combos/selects: lista completa sin paginación. */
    @Query("SELECT r FROM Rol r ORDER BY r.nombre")
    List<Rol> findAllOrdered();

    /** Carga el Rol con sus permisos en una sola query — solo cuando se necesita. */
    @Query("SELECT r FROM Rol r JOIN FETCH r.rolPermisos rp JOIN FETCH rp.permiso WHERE r.id = :id")
    Optional<Rol> findByIdWithPermisos(@Param("id") Long id);
}
