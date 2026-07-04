package com.condominio.condominio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Mapea la tabla {@code usuario_rol} de condominio-db.
 * PK compuesta (id_usuario, id_rol) — igual que rol_permiso, sin id propio.
 * fecha_asignacion la gestiona PostgreSQL DEFAULT now().
 */
@Entity
@Table(name = "usuario_rol")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"usuario", "rol"})
public class UsuarioRol {

    @EmbeddedId
    private UsuarioRolId id = new UsuarioRolId();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("usuarioId")
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("rolId")
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    /** Gestionado por PostgreSQL DEFAULT now() — solo lectura desde JPA. */
    @Column(name = "fecha_asignacion", insertable = false, updatable = false)
    private OffsetDateTime fechaAsignacion;

    // ── Constructor de conveniencia ──────────────────────────────
    public UsuarioRol(Usuario usuario, Rol rol) {
        this.usuario = usuario;
        this.rol = rol;
        this.id = new UsuarioRolId(usuario.getId(), rol.getId());
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioRol other)) return false;
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
    public static class UsuarioRolId implements Serializable {

        @Column(name = "id_usuario")
        private Long usuarioId;

        @Column(name = "id_rol")
        private Long rolId;

        public UsuarioRolId(Long usuarioId, Long rolId) {
            this.usuarioId = usuarioId;
            this.rolId = rolId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof UsuarioRolId other)) return false;
            return Objects.equals(usuarioId, other.usuarioId) &&
                   Objects.equals(rolId, other.rolId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(usuarioId, rolId);
        }
    }
}
