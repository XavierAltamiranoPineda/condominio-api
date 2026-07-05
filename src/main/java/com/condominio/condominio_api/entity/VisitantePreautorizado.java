package com.condominio.condominio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;

@Entity
@Table(name = "visitante_preautorizado")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"visitante", "unidad", "autorizadoPor"})
public class VisitantePreautorizado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_preautorizacion")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_visitante", nullable = false)
    private Visitante visitante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_unidad", nullable = false)
    private Unidad unidad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "autorizado_por", nullable = false)
    private Persona autorizadoPor;

    @Column(name = "fecha_autorizada", insertable = false, updatable = false)
    private OffsetDateTime fechaAutorizada;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VisitantePreautorizado other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
