package com.condominio.condominio_api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservaRequest {

    @NotNull(message = "El id del área común es obligatorio")
    private Long areaId;

    @NotNull(message = "El id de la persona es obligatorio")
    private Long personaId;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    private String motivo;

    private String observaciones;

    private Boolean bloqueaHorario = true;
}
