package com.condominio.condominio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.OffsetDateTime;

@Entity
@Table(name = "comunicado")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"autor"})
public class Comunicado {

    public enum DestinatarioTipo {
        TODOS, TORRE, UNIDAD, ROL
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comunicado")
    private Long id;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "mensaje", nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "fecha", insertable = false, updatable = false)
    private OffsetDateTime fecha;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_autor", nullable = false)
    private Persona autor;

    @Enumerated(EnumType.STRING)
    @Column(name = "destinatario_tipo", nullable = false, columnDefinition = "destinatario_tipo_enum")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private DestinatarioTipo destinatarioTipo;

    @Column(name = "destinatario_id")
    private Long destinatarioId;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comunicado other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
