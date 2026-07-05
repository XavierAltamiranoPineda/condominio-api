package com.condominio.condominio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;

@Entity
@Table(name = "archivo")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Archivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_archivo")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    @Column(name = "ruta", nullable = false, columnDefinition = "TEXT")
    private String ruta;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "tamano")
    private Long tamano;

    @Column(name = "fecha", insertable = false, updatable = false)
    private OffsetDateTime fecha;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Archivo other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
