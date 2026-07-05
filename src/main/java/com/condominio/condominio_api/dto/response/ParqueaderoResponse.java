package com.condominio.condominio_api.dto.response;

import com.condominio.condominio_api.entity.Parqueadero;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParqueaderoResponse {
    private Long id;
    private Long unidadId;
    private String unidadNumero;
    private String numero;
    private Parqueadero.EstadoParqueadero estado;
}
