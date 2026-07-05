package com.condominio.condominio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "configuracion")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Configuracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_configuracion")
    private Long id;

    @Column(name = "clave", nullable = false, unique = true, length = 100)
    private String clave;

    @Column(name = "valor", nullable = false, columnDefinition = "TEXT")
    private String valor;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Configuracion other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
