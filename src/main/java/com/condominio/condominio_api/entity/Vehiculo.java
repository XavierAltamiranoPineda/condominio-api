package com.condominio.condominio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Entity
@Table(name = "vehiculo")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"unidad", "personaActual"})
public class Vehiculo {

    public enum TipoVehiculo {
        AUTO, CAMIONETA, MOTO, BICICLETA, SCOOTER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_unidad", nullable = false)
    private Unidad unidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona_actual")
    private Persona personaActual;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, columnDefinition = "tipo_vehiculo_enum")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private TipoVehiculo tipo;

    @Column(name = "placa", length = 15)
    private String placa;

    @Column(name = "marca", length = 50)
    private String marca;

    @Column(name = "modelo", length = 50)
    private String modelo;

    @Column(name = "color", length = 30)
    private String color;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehiculo other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
