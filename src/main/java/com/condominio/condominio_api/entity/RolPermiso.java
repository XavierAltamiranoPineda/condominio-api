package com.condominio.condominio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

/**
 * Mapea la tabla {@code rol_permiso} de condominio-db.
 * PK compuesta (id_rol, id_permiso) — la tabla no tiene columna id propia.
 * Se representa con una clase @Embeddable para la clave compuesta.
 */
@Entity
@Table(name = "rol_permiso")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"rol", "permiso"})
public class RolPermiso {

    @EmbeddedId
    private RolPermisoId id = new RolPermisoId();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("rolId")
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("permisoId")
    @JoinColumn(name = "id_permiso", nullable = false)
    private Permiso permiso;

    // ── Constructor de conveniencia ──────────────────────────────
    public RolPermiso(Rol rol, Permiso permiso) {
        this.rol = rol;
        this.permiso = permiso;
        this.id = new RolPermisoId(rol.getId(), permiso.getId());
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RolPermiso other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }

    // ── Clave compuesta embebida ─────────────────────────────────
    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    public static class RolPermisoId implements Serializable {

        @Column(name = "id_rol")
        private Long rolId;

        @Column(name = "id_permiso")
        private Long permisoId;

        public RolPermisoId(Long rolId, Long permisoId) {
            this.rolId = rolId;
            this.permisoId = permisoId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RolPermisoId other)) return false;
            return Objects.equals(rolId, other.rolId) &&
                   Objects.equals(permisoId, other.permisoId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(rolId, permisoId);
        }
    }
}
