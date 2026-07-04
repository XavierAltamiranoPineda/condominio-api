package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.UsuarioRol;
import com.condominio.condominio_api.entity.UsuarioRol.UsuarioRolId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, UsuarioRolId> {

    List<UsuarioRol> findByIdUsuarioId(Long usuarioId);

    boolean existsByIdUsuarioIdAndIdRolId(Long usuarioId, Long rolId);

    @Modifying
    @Query("DELETE FROM UsuarioRol ur WHERE ur.id.usuarioId = :usuarioId AND ur.id.rolId = :rolId")
    void deleteByUsuarioIdAndRolId(@Param("usuarioId") Long usuarioId, @Param("rolId") Long rolId);
}
