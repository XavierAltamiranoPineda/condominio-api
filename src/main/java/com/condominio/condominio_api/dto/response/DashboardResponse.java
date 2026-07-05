package com.condominio.condominio_api.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DashboardResponse {
    private long ticketsAbiertos;
    private long cuotasVencidas;
    private BigDecimal montoCuotasVencidas;
    private BigDecimal recaudacionMesActual;
    private BigDecimal saldoPendienteTotal;
    private List<RecaudacionMensualDTO> recaudacionUltimos6Meses;
}
