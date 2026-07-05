package com.condominio.condominio_api.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class RecaudacionMensualDTO {
    private String mesAnio;
    private BigDecimal total;
}
