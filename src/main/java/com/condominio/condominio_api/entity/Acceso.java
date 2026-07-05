package com.condominio.condominio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;

@Entity
@Table(name = "acceso")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"visitante", "unidad", "guardia", "preautorizacion"})
public class Acceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_acceso")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_visitante", nullable = false)
    private Visitante visitante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_unidad", nullable = false)
    private Unidad unidad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_guardia", nullable = false)
    private Persona guardia;

    @Column(name = "id_vehiculo")
    private Long vehiculoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_preautorizacion")
    private VisitantePreautorizado preautorizacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_estado", nullable = false)
    private EstadoAcceso estado;

    @Column(name = "hora_ingreso", insertable = false, updatable = false)
    private OffsetDateTime horaIngreso;

    @Column(name = "hora_salida")
    private OffsetDateTime horaSalida;

    @Column(name = "foto", columnDefinition = "TEXT")
    private String foto;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Acceso other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
