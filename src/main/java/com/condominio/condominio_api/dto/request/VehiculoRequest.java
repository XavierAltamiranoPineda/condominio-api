package com.condominio.condominio_api.dto.request;

import com.condominio.condominio_api.entity.Vehiculo;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VehiculoRequest {

    @NotNull(message = "El id de la unidad es obligatorio")
    private Long unidadId;

    private Long personaId; // Opcional, puede estar parqueado pero no asociado a alguien específico o visitante

    @NotNull(message = "El tipo de vehículo es obligatorio")
    private Vehiculo.TipoVehiculo tipo;

    private String placa;
    private String marca;
    private String modelo;
    private String color;
}
