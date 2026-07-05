package com.condominio.condominio_api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class AccesoResponse {
    private Long id;
    private Long visitanteId;
    private String visitanteNombre;
    private String visitanteCedula;
    private Long unidadId;
    private String unidadNumero;
    private Long guardiaId;
    private String guardiaNombres;
    private Long preautorizacionId;
    private Long estadoId;
    private String estadoNombre;
    private Long vehiculoId;
    private OffsetDateTime horaIngreso;
    private OffsetDateTime horaSalida;
    private String foto;
}
