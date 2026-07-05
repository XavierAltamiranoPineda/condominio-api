package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.ConfiguracionRequest;
import com.condominio.condominio_api.dto.response.ConfiguracionResponse;
import java.util.List;

public interface ConfiguracionService {
    ConfiguracionResponse findById(Long id);
    ConfiguracionResponse findByClave(String clave);
    List<ConfiguracionResponse> findAll();
    ConfiguracionResponse create(ConfiguracionRequest request);
    ConfiguracionResponse update(Long id, ConfiguracionRequest request);
    void delete(Long id);
}
