package com.condominio.condominio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapea la tabla {@code persona} de condominio-db.
 * Persona es una entidad independiente que puede o no tener Usuario.
 * PK: id_persona (BIGSERIAL).
 */
@Entity
@Table(name = "persona")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"usuario"})
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_identificacion", nullable = false,
            columnDefinition = "tipo_identificacion_enum")
    private TipoIdentificacion tipoIdentificacion;

    @Column(name = "numero_identificacion", nullable = false, length = 30)
    private String numeroIdentificacion;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "telefono", length = 30)
    private String telefono;

    @Column(name = "correo", nullable = false, unique = true, length = 254)
    private String correo;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "foto_perfil", columnDefinition = "TEXT")
    private String fotoPerfil;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, columnDefinition = "estado_persona_enum")
    private EstadoPersona estado = EstadoPersona.ACTIVO;

    // Lado inverso — 1 persona puede tener 1 usuario
    @OneToOne(mappedBy = "persona", fetch = FetchType.LAZY)
    private Usuario usuario;

    // Lado inverso — relaciones con unidades (Descomentar al implementar PersonaUnidad)
    // @OneToMany(mappedBy = "persona", fetch = FetchType.LAZY)
    // private List<PersonaUnidad> personaUnidades = new ArrayList<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Persona other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }

    // ── Enums internos ───────────────────────────────────────────
    public enum TipoIdentificacion { CEDULA, PASAPORTE, RUC }
    public enum EstadoPersona { ACTIVO, INACTIVO }
}
