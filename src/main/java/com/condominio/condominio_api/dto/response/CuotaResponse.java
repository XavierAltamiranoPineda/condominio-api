package com.condominio.condominio_api.dto.response;

import com.condominio.condominio_api.entity.Cuota.EstadoCuota;
import com.condominio.condominio_api.entity.Cuota.TipoCuota;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuotaResponse {
    private Long id;
    private Long unidadId;
    private String unidadNumero;
    private String condominioNombre;
    private Short mes;
    private Short anio;
    private BigDecimal valor;
    private TipoCuota tipo;
    private String descripcion;
    private LocalDate fechaVencimiento;
    private EstadoCuota estado;
}
