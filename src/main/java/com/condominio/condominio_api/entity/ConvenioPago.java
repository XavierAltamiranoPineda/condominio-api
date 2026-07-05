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
@Table(name = "convenio_pago")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"persona", "unidad"})
public class ConvenioPago {

    public enum EstadoConvenio {
        ACTIVO, COMPLETADO, INCUMPLIDO, ANULADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_convenio")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_unidad", nullable = false)
    private Unidad unidad;

    @Column(name = "monto_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @Column(name = "num_cuotas", nullable = false)
    private Short numCuotas;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, columnDefinition = "estado_convenio_enum")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private EstadoConvenio estado = EstadoConvenio.ACTIVO;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConvenioPago other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
