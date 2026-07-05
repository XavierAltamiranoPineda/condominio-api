package com.condominio.condominio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Entity
@Table(name = "parqueadero")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"unidad"})
public class Parqueadero {

    public enum EstadoParqueadero {
        DISPONIBLE, OCUPADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_parqueadero")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_unidad", nullable = false)
    private Unidad unidad;

    @Column(name = "numero", nullable = false, length = 20)
    private String numero;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, columnDefinition = "estado_parqueadero_enum")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private EstadoParqueadero estado = EstadoParqueadero.DISPONIBLE;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parqueadero other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
