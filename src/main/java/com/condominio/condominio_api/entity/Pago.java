package com.condominio.condominio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "pago")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"cuota", "estado"})
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cuota", nullable = false)
    private Cuota cuota;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_estado", nullable = false)
    private EstadoPago estado;

    @Column(name = "fecha", nullable = false)
    private OffsetDateTime fecha;

    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "metodo", nullable = false, length = 50)
    private String metodo;

    @Column(name = "referencia", length = 100)
    private String referencia;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pago other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
