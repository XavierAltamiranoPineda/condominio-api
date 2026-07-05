package com.condominio.condominio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "multa")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"unidad", "persona", "cuota"})
public class Multa {

    public enum EstadoMulta {
        REGISTRADA, FACTURADA, ANULADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_multa")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_unidad", nullable = false)
    private Unidad unidad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cuota", unique = true)
    private Cuota cuota;

    @Column(name = "motivo", nullable = false, length = 150)
    private String motivo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "fecha", insertable = false, updatable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, columnDefinition = "estado_multa_enum")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private EstadoMulta estado = EstadoMulta.REGISTRADA;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Multa other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
