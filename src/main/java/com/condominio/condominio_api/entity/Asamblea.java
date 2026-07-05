package com.condominio.condominio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "asamblea")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"condominio"})
public class Asamblea {

    public enum TipoAsamblea {
        ORDINARIA, EXTRAORDINARIA
    }

    public enum EstadoAsamblea {
        PROGRAMADA, EN_CURSO, FINALIZADA, CANCELADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asamblea")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_condominio", nullable = false)
    private Condominio condominio;

    @Column(name = "fecha", nullable = false)
    private OffsetDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, columnDefinition = "tipo_asamblea_enum")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private TipoAsamblea tipo;

    @Column(name = "quorum_requerido", precision = 5, scale = 2)
    private BigDecimal quorumRequerido;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, columnDefinition = "estado_asamblea_enum")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private EstadoAsamblea estado = EstadoAsamblea.PROGRAMADA;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Asamblea other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
