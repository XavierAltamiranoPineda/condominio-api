package com.condominio.condominio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapea la tabla {@code permiso} de condominio-db.
 * PK: id_permiso (BIGSERIAL). Sin UUID (sistema cerrado por JWT).
 * Auditoría delegada a PostgreSQL (no extiende BaseAuditEntity).
 */
@Entity
@Table(name = "permiso")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"rolPermisos"})
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_permiso")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /** Módulo del sistema al que aplica (RESIDENTES, CUOTAS, PAGOS…). */
    @Column(name = "modulo", nullable = false, length = 50)
    private String modulo;

    /** Acción permitida: CREAR, LEER, EDITAR, ELIMINAR. */
    @Column(name = "accion", nullable = false, length = 50)
    private String accion;

    // Lado inverso — solo si se necesita navegar desde Permiso → roles
    @OneToMany(mappedBy = "permiso", fetch = FetchType.LAZY)
    private List<RolPermiso> rolPermisos = new ArrayList<>();

    // ── equals / hashCode basado en id ──────────────────────────
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permiso other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
