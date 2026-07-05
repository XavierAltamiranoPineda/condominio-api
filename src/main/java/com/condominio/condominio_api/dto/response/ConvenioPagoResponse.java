package com.condominio.condominio_api.dto.response;

import com.condominio.condominio_api.entity.ConvenioPago;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ConvenioPagoResponse {
    private Long id;
    private Long personaId;
    private String personaNombres;
    private String personaApellidos;
    private Long unidadId;
    private String unidadNumero;
    private BigDecimal montoTotal;
    private Short numCuotas;
    private LocalDate fechaInicio;
    private ConvenioPago.EstadoConvenio estado;
}
