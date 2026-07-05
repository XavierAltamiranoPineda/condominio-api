package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.AccesoRequest;
import com.condominio.condominio_api.dto.response.AccesoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface AccesoService {
    AccesoResponse findById(Long id);
    Page<AccesoResponse> findByCondominioId(Long condominioId, Pageable pageable);
    Page<AccesoResponse> findByUnidadId(Long unidadId, Pageable pageable);
    AccesoResponse registrarIngreso(AccesoRequest request, MultipartFile foto);
    AccesoResponse registrarSalida(Long id);
    AccesoResponse registrarDenegado(AccesoRequest request);
}
