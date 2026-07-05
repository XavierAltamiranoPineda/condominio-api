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
@Table(name = "notificacion")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"persona"})
public class Notificacion {

    public enum CanalNotificacion {
        EMAIL, PUSH, SMS, WHATSAPP
    }

    public enum EstadoEnvio {
        PENDIENTE, ENVIADO, FALLIDO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "mensaje", nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Enumerated(EnumType.STRING)
    @Column(name = "canal", nullable = false, columnDefinition = "canal_notificacion_enum")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private CanalNotificacion canal;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_envio", nullable = false, columnDefinition = "estado_envio_enum")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private EstadoEnvio estadoEnvio = EstadoEnvio.PENDIENTE;

    @Column(name = "fecha_envio")
    private OffsetDateTime fechaEnvio;

    @Column(name = "leido", nullable = false)
    private Boolean leido = false;

    @Column(name = "fecha_lectura")
    private OffsetDateTime fechaLectura;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notificacion other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
