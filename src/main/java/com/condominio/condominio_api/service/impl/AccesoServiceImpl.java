package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.AccesoRequest;
import com.condominio.condominio_api.dto.response.AccesoResponse;
import com.condominio.condominio_api.entity.*;
import com.condominio.condominio_api.exception.BusinessException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.AccesoMapper;
import com.condominio.condominio_api.mapper.VisitanteMapper;
import com.condominio.condominio_api.repository.*;
import com.condominio.condominio_api.service.interfaces.AccesoService;
import com.condominio.condominio_api.service.interfaces.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccesoServiceImpl implements AccesoService {

    private final AccesoRepository accesoRepository;
    private final VisitanteRepository visitanteRepository;
    private final UnidadRepository unidadRepository;
    private final PersonaRepository personaRepository;
    private final EstadoAccesoRepository estadoAccesoRepository;
    private final VisitantePreautorizadoRepository preautorizadoRepository;
    private final AccesoMapper accesoMapper;
    private final VisitanteMapper visitanteMapper;
    private final PostgresAuditInterceptor auditInterceptor;
    private final StorageService storageService;

    @Override
    public AccesoResponse findById(Long id) {
        return accesoRepository.findByIdWithDetails(id)
                .map(accesoMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Acceso", "id", id));
    }

    @Override
    public Page<AccesoResponse> findByCondominioId(Long condominioId, Pageable pageable) {
        return accesoRepository.findByCondominioIdWithDetails(condominioId, pageable)
                .map(accesoMapper::toResponse);
    }

    @Override
    public Page<AccesoResponse> findByUnidadId(Long unidadId, Pageable pageable) {
        return accesoRepository.findByUnidadIdWithDetails(unidadId, pageable)
                .map(accesoMapper::toResponse);
    }

    @Override
    @Transactional
    public AccesoResponse registrarIngreso(AccesoRequest request, MultipartFile foto) {
        Visitante visitante = resolveVisitante(request);

        Unidad unidad = unidadRepository.findById(request.getUnidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Unidad", "id", request.getUnidadId()));

        Persona guardia = personaRepository.findById(request.getGuardiaId())
                .orElseThrow(() -> new ResourceNotFoundException("Persona (Guardia)", "id", request.getGuardiaId()));

        VisitantePreautorizado preautorizado = null;
        if (request.getPreautorizacionId() != null) {
            preautorizado = preautorizadoRepository.findById(request.getPreautorizacionId())
                    .orElseThrow(() -> new ResourceNotFoundException("VisitantePreautorizado", "id", request.getPreautorizacionId()));
        }

        EstadoAcceso estadoEnCurso = estadoAccesoRepository.findByNombreIgnoreCase("EN_CURSO")
                .orElseThrow(() -> new BusinessException("Estado EN_CURSO no encontrado"));

        String fotoPath = null;
        if (foto != null && !foto.isEmpty()) {
            fotoPath = storageService.store(foto);
        }

        auditInterceptor.setUsuarioActual();
        Acceso acceso = accesoMapper.toEntity(request);
        acceso.setVisitante(visitante);
        acceso.setUnidad(unidad);
        acceso.setGuardia(guardia);
        acceso.setPreautorizacion(preautorizado);
        acceso.setEstado(estadoEnCurso);
        acceso.setFoto(fotoPath);
        
        acceso = accesoRepository.save(acceso);
        log.info("Ingreso registrado: accesoId={}, visitanteId={}", acceso.getId(), visitante.getId());
        return accesoMapper.toResponse(acceso);
    }

    @Override
    @Transactional
    public AccesoResponse registrarSalida(Long id) {
        Acceso acceso = accesoRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Acceso", "id", id));

        if (acceso.getHoraSalida() != null) {
            throw new BusinessException("El acceso ya tiene registrada una hora de salida");
        }

        EstadoAcceso estadoFinalizado = estadoAccesoRepository.findByNombreIgnoreCase("FINALIZADO")
                .orElseThrow(() -> new BusinessException("Estado FINALIZADO no encontrado"));

        auditInterceptor.setUsuarioActual();
        acceso.setEstado(estadoFinalizado);
        acceso.setHoraSalida(OffsetDateTime.now());
        
        acceso = accesoRepository.save(acceso);
        log.info("Salida registrada: accesoId={}", acceso.getId());
        return accesoMapper.toResponse(acceso);
    }

    @Override
    @Transactional
    public AccesoResponse registrarDenegado(AccesoRequest request) {
        Visitante visitante = resolveVisitante(request);

        Unidad unidad = unidadRepository.findById(request.getUnidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Unidad", "id", request.getUnidadId()));

        Persona guardia = personaRepository.findById(request.getGuardiaId())
                .orElseThrow(() -> new ResourceNotFoundException("Persona (Guardia)", "id", request.getGuardiaId()));

        EstadoAcceso estadoDenegado = estadoAccesoRepository.findByNombreIgnoreCase("DENEGADO")
                .orElseThrow(() -> new BusinessException("Estado DENEGADO no encontrado"));

        auditInterceptor.setUsuarioActual();
        Acceso acceso = accesoMapper.toEntity(request);
        acceso.setVisitante(visitante);
        acceso.setUnidad(unidad);
        acceso.setGuardia(guardia);
        acceso.setEstado(estadoDenegado);
        acceso.setHoraSalida(OffsetDateTime.now()); // Para denegados, la salida es inmediata
        
        acceso = accesoRepository.save(acceso);
        log.info("Acceso denegado registrado: accesoId={}, visitanteId={}", acceso.getId(), visitante.getId());
        return accesoMapper.toResponse(acceso);
    }
    
    private Visitante resolveVisitante(AccesoRequest request) {
        if (request.getVisitanteId() != null) {
            return visitanteRepository.findById(request.getVisitanteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Visitante", "id", request.getVisitanteId()));
        } else if (request.getVisitanteNuevo() != null) {
            if (request.getVisitanteNuevo().getCedula() != null) {
                var existente = visitanteRepository.findByCedula(request.getVisitanteNuevo().getCedula());
                if (existente.isPresent()) {
                    return existente.get();
                }
            }
            Visitante visitante = visitanteMapper.toEntity(request.getVisitanteNuevo());
            return visitanteRepository.save(visitante);
        } else {
            throw new BusinessException("Debe proveer visitanteId o visitanteNuevo");
        }
    }
}
