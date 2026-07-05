package com.condominio.condominio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.OffsetDateTime;

@Entity
@Table(name = "votacion", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_asamblea", "id_persona"})
})
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"asamblea", "persona"})
public class Votacion {

    public enum OpcionVotacion {
        A_FAVOR, EN_CONTRA, ABSTENCION
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_votacion")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_asamblea", nullable = false)
    private Asamblea asamblea;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;

    @Enumerated(EnumType.STRING)
    @Column(name = "opcion", nullable = false, columnDefinition = "opcion_votacion_enum")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private OpcionVotacion opcion;

    @Column(name = "fecha", insertable = false, updatable = false)
    private OffsetDateTime fecha;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Votacion other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
