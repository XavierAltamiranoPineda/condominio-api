package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.ComunicadoLecturaRequest;
import com.condominio.condominio_api.dto.request.ComunicadoRequest;
import com.condominio.condominio_api.dto.response.ComunicadoResponse;
import com.condominio.condominio_api.entity.Comunicado;
import com.condominio.condominio_api.entity.ComunicadoLectura;
import com.condominio.condominio_api.entity.ComunicadoLecturaId;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.exception.BusinessException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.ComunicadoMapper;
import com.condominio.condominio_api.repository.ComunicadoLecturaRepository;
import com.condominio.condominio_api.repository.ComunicadoRepository;
import com.condominio.condominio_api.repository.PersonaRepository;
import com.condominio.condominio_api.service.interfaces.ComunicadoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComunicadoServiceImpl implements ComunicadoService {

    private final ComunicadoRepository comunicadoRepository;
    private final ComunicadoLecturaRepository lecturaRepository;
    private final PersonaRepository personaRepository;
    private final ComunicadoMapper comunicadoMapper;
    private final PostgresAuditInterceptor auditInterceptor;

    @Override
    public ComunicadoResponse findById(Long id) {
        return comunicadoRepository.findByIdWithAutor(id)
                .map(comunicadoMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Comunicado", "id", id));
    }

    @Override
    public Page<ComunicadoResponse> findAll(Pageable pageable) {
        return comunicadoRepository.findAllWithAutor(pageable)
                .map(comunicadoMapper::toResponse);
    }

    @Override
    @Transactional
    public ComunicadoResponse create(ComunicadoRequest request) {
        Persona autor = personaRepository.findById(request.getAutorId())
                .orElseThrow(() -> new ResourceNotFoundException("Persona (Autor)", "id", request.getAutorId()));

        auditInterceptor.setUsuarioActual();
        Comunicado comunicado = comunicadoMapper.toEntity(request);
        comunicado.setAutor(autor);
        
        comunicado = comunicadoRepository.save(comunicado);
        log.info("Comunicado publicado: id={}, titulo={}", comunicado.getId(), comunicado.getTitulo());
        return comunicadoMapper.toResponse(comunicado);
    }

    @Override
    @Transactional
    public ComunicadoResponse update(Long id, ComunicadoRequest request) {
        Comunicado comunicado = comunicadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comunicado", "id", id));

        auditInterceptor.setUsuarioActual();
        comunicadoMapper.updateEntityFromRequest(request, comunicado);
        
        if (request.getAutorId() != null && !comunicado.getAutor().getId().equals(request.getAutorId())) {
            Persona autor = personaRepository.findById(request.getAutorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Persona (Autor)", "id", request.getAutorId()));
            comunicado.setAutor(autor);
        }

        comunicado = comunicadoRepository.save(comunicado);
        log.info("Comunicado actualizado: id={}", comunicado.getId());
        return comunicadoMapper.toResponse(comunicado);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Comunicado comunicado = comunicadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comunicado", "id", id));
        
        auditInterceptor.setUsuarioActual();
        comunicadoRepository.delete(comunicado);
        log.info("Comunicado eliminado: id={}", id);
    }

    @Override
    @Transactional
    public void marcarComoLeido(ComunicadoLecturaRequest request) {
        Comunicado comunicado = comunicadoRepository.findById(request.getComunicadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Comunicado", "id", request.getComunicadoId()));
                
        Persona persona = personaRepository.findById(request.getPersonaId())
                .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", request.getPersonaId()));

        ComunicadoLecturaId lecturaId = new ComunicadoLecturaId(comunicado.getId(), persona.getId());
        
        if (lecturaRepository.existsById(lecturaId)) {
            throw new BusinessException("El comunicado ya fue marcado como leído por esta persona");
        }

        auditInterceptor.setUsuarioActual();
        ComunicadoLectura lectura = new ComunicadoLectura();
        lectura.setId(lecturaId);
        lectura.setComunicado(comunicado);
        lectura.setPersona(persona);
        
        lecturaRepository.save(lectura);
        log.info("Comunicado leído: comunicadoId={}, personaId={}", comunicado.getId(), persona.getId());
    }

    @Override
    public boolean estaLeido(Long comunicadoId, Long personaId) {
        return lecturaRepository.existsByIdComunicadoAndIdPersona(comunicadoId, personaId);
    }
}
