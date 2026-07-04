package com.condominio.condominio_api.entity;

import com.condominio.condominio_api.entity.enums.TipoUnidadEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;

@Entity
@Table(name = "unidad")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"condominio", "torre", "estado"})
public class Unidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_unidad")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_condominio", nullable = false)
    private Condominio condominio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_torre")
    private Torre torre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_estado", nullable = false)
    private EstadoUnidad estado;

    @Column(name = "numero", nullable = false, length = 20)
    private String numero;

    @Column(name = "piso", length = 10)
    private String piso;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private TipoUnidadEnum tipo;

    @Column(name = "alicuota", precision = 8, scale = 6)
    private BigDecimal alicuota;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Unidad other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
