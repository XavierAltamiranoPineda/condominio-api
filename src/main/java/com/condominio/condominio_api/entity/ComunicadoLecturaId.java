package com.condominio.condominio_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComunicadoLecturaId implements Serializable {

    @Column(name = "id_comunicado")
    private Long idComunicado;

    @Column(name = "id_persona")
    private Long idPersona;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComunicadoLecturaId other)) return false;
        return Objects.equals(idComunicado, other.idComunicado) &&
               Objects.equals(idPersona, other.idPersona);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idComunicado, idPersona);
    }
}
