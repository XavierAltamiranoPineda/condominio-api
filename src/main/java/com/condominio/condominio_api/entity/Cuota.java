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
@Table(name = "cuota")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"unidad"})
public class Cuota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuota")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_unidad", nullable = false)
    private Unidad unidad;

    @Column(name = "mes", nullable = false)
    private Short mes;

    @Column(name = "anio", nullable = false)
    private Short anio;

    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private TipoCuota tipo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private EstadoCuota estado = EstadoCuota.PENDIENTE;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cuota other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }

    public enum TipoCuota { ORDINARIA, EXTRAORDINARIA, MULTA }
    public enum EstadoCuota { PENDIENTE, PAGADA_PARCIAL, PAGADA_TOTAL, ANULADA }
}
