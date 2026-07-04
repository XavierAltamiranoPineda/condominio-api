package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.Unidad;
import com.condominio.condominio_api.entity.enums.TipoUnidadEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UnidadRepository extends JpaRepository<Unidad, Long> {

    @Query("SELECT u FROM Unidad u JOIN FETCH u.condominio JOIN FETCH u.estado LEFT JOIN FETCH u.torre WHERE u.id = :id")
    java.util.Optional<Unidad> findByIdWithDetails(@Param("id") Long id);

    @Query(value = "SELECT u FROM Unidad u JOIN FETCH u.condominio JOIN FETCH u.estado LEFT JOIN FETCH u.torre",
           countQuery = "SELECT count(u) FROM Unidad u")
    Page<Unidad> findAllWithDetails(Pageable pageable);

    @Query(value = "SELECT u FROM Unidad u JOIN FETCH u.condominio JOIN FETCH u.estado LEFT JOIN FETCH u.torre WHERE u.estado.nombre = :estado",
           countQuery = "SELECT count(u) FROM Unidad u WHERE u.estado.nombre = :estado")
    Page<Unidad> findByEstadoWithDetails(@Param("estado") String estado, Pageable pageable);

    @Query(value = "SELECT u FROM Unidad u JOIN FETCH u.condominio JOIN FETCH u.estado LEFT JOIN FETCH u.torre WHERE u.tipo = :tipo",
           countQuery = "SELECT count(u) FROM Unidad u WHERE u.tipo = :tipo")
    Page<Unidad> findByTipoWithDetails(@Param("tipo") TipoUnidadEnum tipo, Pageable pageable);

    @Query(value = "SELECT u FROM Unidad u JOIN FETCH u.condominio JOIN FETCH u.estado LEFT JOIN FETCH u.torre WHERE u.torre.id = :torreId",
           countQuery = "SELECT count(u) FROM Unidad u WHERE u.torre.id = :torreId")
    Page<Unidad> findByTorreIdWithDetails(@Param("torreId") Long torreId, Pageable pageable);

    boolean existsByCondominioIdAndNumeroIgnoreCase(Long condominioId, String numero);
    boolean existsByCondominioIdAndNumeroIgnoreCaseAndIdNot(Long condominioId, String numero, Long id);
}
