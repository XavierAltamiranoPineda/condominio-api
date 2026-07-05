package com.condominio.condominio_api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@Data
@Builder
public class ReservaResponse {
    private Long id;
    private Long areaId;
    private String areaNombre;
    private Long personaId;
    private String personaNombres;
    private String personaApellidos;
    private Long estadoId;
    private String estadoNombre;
    private Long usuarioAprobadorId;
    private String usuarioAprobadorUsername;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private OffsetDateTime fechaCreacion;
    private String motivo;
    private String observaciones;
    private Boolean bloqueaHorario;
}
