package com.condominio.condominio_api.dto.request;

import com.condominio.condominio_api.entity.PersonaUnidad.EstadoPersonaUnidad;
import com.condominio.condominio_api.entity.PersonaUnidad.TipoPersonaUnidad;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PersonaUnidadRequest {

    @NotNull(message = "El id de persona es obligatorio")
    private Long personaId;

    @NotNull(message = "El id de unidad es obligatorio")
    private Long unidadId;

    @NotNull(message = "El tipo es obligatorio")
    private TipoPersonaUnidad tipo;

    @NotNull(message = "El estado es obligatorio")
    private EstadoPersonaUnidad estado;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    private LocalDate fechaFin;
}
