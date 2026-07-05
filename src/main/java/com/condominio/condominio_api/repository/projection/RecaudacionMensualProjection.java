package com.condominio.condominio_api.repository.projection;

import java.math.BigDecimal;

public interface RecaudacionMensualProjection {
    Integer getAnio();
    Integer getMes();
    BigDecimal getTotal();
}
