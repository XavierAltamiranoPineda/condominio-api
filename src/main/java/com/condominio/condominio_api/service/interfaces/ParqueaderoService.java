package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.ParqueaderoRequest;
import com.condominio.condominio_api.dto.response.ParqueaderoResponse;
import com.condominio.condominio_api.entity.Parqueadero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ParqueaderoService {
    ParqueaderoResponse findById(Long id);
    List<ParqueaderoResponse> findByUnidadId(Long unidadId);
    Page<ParqueaderoResponse> findByCondominioId(Long condominioId, Pageable pageable);
    ParqueaderoResponse create(ParqueaderoRequest request);
    ParqueaderoResponse update(Long id, ParqueaderoRequest request);
    ParqueaderoResponse cambiarEstado(Long id, Parqueadero.EstadoParqueadero estado);
    void delete(Long id);
}
