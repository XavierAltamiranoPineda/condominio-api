package com.condominio.condominio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "torre")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"condominio"})
public class Torre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_torre")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_condominio", nullable = false)
    private Condominio condominio;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Torre other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
