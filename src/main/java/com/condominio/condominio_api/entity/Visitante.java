package com.condominio.condominio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "visitante")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Visitante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_visitante")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "cedula", length = 30)
    private String cedula;

    @Column(name = "telefono", length = 30)
    private String telefono;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Visitante other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
