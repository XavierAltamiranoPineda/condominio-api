package com.condominio.condominio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;

@Entity
@Table(name = "comunicado_lectura")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"comunicado", "persona"})
public class ComunicadoLectura {

    @EmbeddedId
    private ComunicadoLecturaId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("idComunicado")
    @JoinColumn(name = "id_comunicado")
    private Comunicado comunicado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("idPersona")
    @JoinColumn(name = "id_persona")
    private Persona persona;

    @Column(name = "fecha_lectura", insertable = false, updatable = false)
    private OffsetDateTime fechaLectura;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComunicadoLectura other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
