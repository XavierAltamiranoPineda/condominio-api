package com.condominio.condominio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapea la tabla {@code usuario} de condominio-db.
 * PK: id_usuario (BIGSERIAL).
 * Requiere obligatoriamente una {@link Persona} asociada (1:1 único).
 * Las columnas de auditoría (fecha_creacion) son gestionadas por PostgreSQL DEFAULT.
 */
@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"persona", "usuarioRoles"})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    /** Relación 1:1 obligatoria con Persona. Persona existe antes que Usuario. */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_persona", nullable = false, unique = true)
    private Persona persona;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false, columnDefinition = "TEXT")
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, columnDefinition = "estado_usuario_enum")
    private EstadoUsuario estado = EstadoUsuario.ACTIVO;

    /** Gestionado por PostgreSQL DEFAULT now() — solo lectura desde JPA. */
    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private OffsetDateTime fechaCreacion;

    @Column(name = "ultimo_login")
    private OffsetDateTime ultimoLogin;

    @Column(name = "intentos_fallidos", nullable = false)
    private Integer intentosFallidos = 0;

    @Column(name = "bloqueado_hasta")
    private OffsetDateTime bloqueadoHasta;

    /** UUID para recuperación de contraseña — único caso de uso de token público. */
    @Column(name = "token_recuperacion", columnDefinition = "TEXT")
    private String tokenRecuperacion;

    @Column(name = "fecha_expiracion_token")
    private OffsetDateTime fechaExpiracionToken;

    @OneToMany(mappedBy = "usuario",
               cascade = {CascadeType.PERSIST, CascadeType.MERGE},
               fetch = FetchType.LAZY)
    private List<UsuarioRol> usuarioRoles = new ArrayList<>();

    // ── Métodos helper para relación bidireccional ───────────────
    public void addRol(UsuarioRol usuarioRol) {
        usuarioRoles.add(usuarioRol);
        usuarioRol.setUsuario(this);
    }

    public void removeRol(UsuarioRol usuarioRol) {
        usuarioRoles.remove(usuarioRol);
        usuarioRol.setUsuario(null);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }

    // ── Enum interno ─────────────────────────────────────────────
    public enum EstadoUsuario { ACTIVO, INACTIVO, BLOQUEADO }
}
