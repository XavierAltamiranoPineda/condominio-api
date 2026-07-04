package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.RolPermiso;
import com.condominio.condominio_api.entity.RolPermiso.RolPermisoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RolPermisoRepository extends JpaRepository<RolPermiso, RolPermisoId> {

    List<RolPermiso> findByIdRolId(Long rolId);

    boolean existsByIdRolIdAndIdPermisoId(Long rolId, Long permisoId);

    @Modifying
    @Query("DELETE FROM RolPermiso rp WHERE rp.id.rolId = :rolId AND rp.id.permisoId = :permisoId")
    void deleteByRolIdAndPermisoId(@Param("rolId") Long rolId, @Param("permisoId") Long permisoId);
}
