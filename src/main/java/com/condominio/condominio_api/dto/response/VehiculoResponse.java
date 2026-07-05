package com.condominio.condominio_api.dto.response;

import com.condominio.condominio_api.entity.Vehiculo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VehiculoResponse {
    private Long id;
    private Long unidadId;
    private String unidadNumero;
    private Long personaId;
    private String personaNombres;
    private String personaApellidos;
    private Vehiculo.TipoVehiculo tipo;
    private String placa;
    private String marca;
    private String modelo;
    private String color;
}
