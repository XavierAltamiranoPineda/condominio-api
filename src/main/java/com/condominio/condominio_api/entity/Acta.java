package com.condominio.condominio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "acta")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"asamblea", "archivos"})
public class Acta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_acta")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_asamblea", nullable = false, unique = true)
    private Asamblea asamblea;

    @Column(name = "contenido", columnDefinition = "TEXT")
    private String contenido;

    @ManyToMany
    @JoinTable(
            name = "acta_archivo",
            joinColumns = @JoinColumn(name = "id_acta"),
            inverseJoinColumns = @JoinColumn(name = "id_archivo")
    )
    private List<Archivo> archivos = new ArrayList<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Acta other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
