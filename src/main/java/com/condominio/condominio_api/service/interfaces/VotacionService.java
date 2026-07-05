package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.VotacionRequest;
import com.condominio.condominio_api.dto.response.ResultadosVotacionResponse;
import com.condominio.condominio_api.dto.response.VotacionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VotacionService {
    VotacionResponse findById(Long id);
    Page<VotacionResponse> findByAsambleaId(Long asambleaId, Pageable pageable);
    VotacionResponse emitirVoto(VotacionRequest request);
    ResultadosVotacionResponse obtenerResultados(Long asambleaId);
}
