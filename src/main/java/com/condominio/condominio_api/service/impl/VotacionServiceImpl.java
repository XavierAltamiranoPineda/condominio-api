package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.VotacionRequest;
import com.condominio.condominio_api.dto.response.ResultadosVotacionResponse;
import com.condominio.condominio_api.dto.response.VotacionResponse;
import com.condominio.condominio_api.entity.Asamblea;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.entity.Votacion;
import com.condominio.condominio_api.exception.BusinessException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.VotacionMapper;
import com.condominio.condominio_api.repository.AsambleaRepository;
import com.condominio.condominio_api.repository.PersonaRepository;
import com.condominio.condominio_api.repository.VotacionRepository;
import com.condominio.condominio_api.service.interfaces.VotacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VotacionServiceImpl implements VotacionService {

    private final VotacionRepository votacionRepository;
    private final AsambleaRepository asambleaRepository;
    private final PersonaRepository personaRepository;
    private final VotacionMapper votacionMapper;
    private final PostgresAuditInterceptor auditInterceptor;

    @Override
    public VotacionResponse findById(Long id) {
        return votacionRepository.findById(id)
                .map(votacionMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Votacion", "id", id));
    }

    @Override
    public Page<VotacionResponse> findByAsambleaId(Long asambleaId, Pageable pageable) {
        return votacionRepository.findByAsambleaIdWithDetails(asambleaId, pageable)
                .map(votacionMapper::toResponse);
    }

    @Override
    @Transactional
    public VotacionResponse emitirVoto(VotacionRequest request) {
        Asamblea asamblea = asambleaRepository.findById(request.getAsambleaId())
                .orElseThrow(() -> new ResourceNotFoundException("Asamblea", "id", request.getAsambleaId()));

        if (asamblea.getEstado() != Asamblea.EstadoAsamblea.EN_CURSO) {
            throw new BusinessException("Solo se pueden emitir votos cuando la asamblea está EN_CURSO");
        }

        Persona persona = personaRepository.findById(request.getPersonaId())
                .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", request.getPersonaId()));

        Optional<Votacion> votoExistente = votacionRepository.findByAsambleaIdAndPersonaId(asamblea.getId(), persona.getId());
        if (votoExistente.isPresent()) {
            throw new BusinessException("Esta persona ya ha emitido un voto en esta asamblea");
        }

        auditInterceptor.setUsuarioActual();
        Votacion votacion = votacionMapper.toEntity(request);
        votacion.setAsamblea(asamblea);
        votacion.setPersona(persona);
        
        votacion = votacionRepository.save(votacion);
        log.info("Voto emitido: asambleaId={}, personaId={}", asamblea.getId(), persona.getId());
        return votacionMapper.toResponse(votacion);
    }

    @Override
    public ResultadosVotacionResponse obtenerResultados(Long asambleaId) {
        if (!asambleaRepository.existsById(asambleaId)) {
            throw new ResourceNotFoundException("Asamblea", "id", asambleaId);
        }

        List<Object[]> conteos = votacionRepository.countVotosByOpcion(asambleaId);
        
        long aFavor = 0;
        long enContra = 0;
        long abstencion = 0;
        long totalVotos = 0;

        for (Object[] conteo : conteos) {
            Votacion.OpcionVotacion opcion = (Votacion.OpcionVotacion) conteo[0];
            long cantidad = (Long) conteo[1];
            totalVotos += cantidad;
            
            switch (opcion) {
                case A_FAVOR -> aFavor = cantidad;
                case EN_CONTRA -> enContra = cantidad;
                case ABSTENCION -> abstencion = cantidad;
            }
        }

        return ResultadosVotacionResponse.builder()
                .asambleaId(asambleaId)
                .aFavor(aFavor)
                .enContra(enContra)
                .abstencion(abstencion)
                .totalVotos(totalVotos)
                .build();
    }
}
