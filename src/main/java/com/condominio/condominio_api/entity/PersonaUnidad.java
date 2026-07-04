package com.condominio.condominio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDate;

@Entity
@Table(name = "persona_unidad")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"persona", "unidad"})
public class PersonaUnidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona_unidad")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_unidad", nullable = false)
    private Unidad unidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private TipoPersonaUnidad tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private EstadoPersonaUnidad estado = EstadoPersonaUnidad.ACTIVO;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonaUnidad other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }

    public enum TipoPersonaUnidad { PROPIETARIO, ARRENDATARIO, FAMILIAR, OTRO }
    public enum EstadoPersonaUnidad { ACTIVO, INACTIVO, HISTORICO }
}
