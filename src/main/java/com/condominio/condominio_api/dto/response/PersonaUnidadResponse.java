package com.condominio.condominio_api.dto.response;

import com.condominio.condominio_api.entity.PersonaUnidad.EstadoPersonaUnidad;
import com.condominio.condominio_api.entity.PersonaUnidad.TipoPersonaUnidad;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonaUnidadResponse {
    private Long id;
    private Long personaId;
    private String personaNombres;
    private String personaApellidos;
    private Long unidadId;
    private String unidadNumero;
    private String condominioNombre;
    private TipoPersonaUnidad tipo;
    private EstadoPersonaUnidad estado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
