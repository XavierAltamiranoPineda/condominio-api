package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.ReservaRequest;
import com.condominio.condominio_api.dto.response.ReservaResponse;
import com.condominio.condominio_api.entity.*;
import com.condominio.condominio_api.exception.BusinessException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.ReservaMapper;
import com.condominio.condominio_api.repository.*;
import com.condominio.condominio_api.service.interfaces.ReservaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final AreaComunRepository areaComunRepository;
    private final PersonaRepository personaRepository;
    private final EstadoReservaRepository estadoReservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ReservaMapper reservaMapper;
    private final PostgresAuditInterceptor auditInterceptor;

    @Override
    public ReservaResponse findById(Long id) {
        return reservaRepository.findByIdWithDetails(id)
                .map(reservaMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", "id", id));
    }

    @Override
    public Page<ReservaResponse> findByCondominioId(Long condominioId, Pageable pageable) {
        return reservaRepository.findByCondominioIdWithDetails(condominioId, pageable)
                .map(reservaMapper::toResponse);
    }

    @Override
    public Page<ReservaResponse> findByAreaId(Long areaId, Pageable pageable) {
        return reservaRepository.findByAreaIdWithDetails(areaId, pageable)
                .map(reservaMapper::toResponse);
    }

    @Override
    @Transactional
    public ReservaResponse create(ReservaRequest request) {
        if (!request.getHoraFin().isAfter(request.getHoraInicio())) {
            throw new BusinessException("La hora de fin debe ser posterior a la hora de inicio");
        }
        
        LocalDate fechaActual = LocalDate.now();
        if (request.getFecha().isBefore(fechaActual)) {
            throw new BusinessException("No se pueden crear reservas en el pasado");
        }
        
        if (request.getFecha().isEqual(fechaActual) && request.getHoraInicio().isBefore(java.time.LocalTime.now())) {
            throw new BusinessException("La hora de inicio de la reserva no puede estar en el pasado");
        }

        AreaComun area = areaComunRepository.findById(request.getAreaId())
                .orElseThrow(() -> new ResourceNotFoundException("AreaComun", "id", request.getAreaId()));

        Persona persona = personaRepository.findById(request.getPersonaId())
                .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", request.getPersonaId()));

        if (Boolean.TRUE.equals(request.getBloqueaHorario())) {
            long superposiciones = reservaRepository.countSuperposiciones(
                    area.getId(), request.getFecha(), request.getHoraInicio(), request.getHoraFin());
            if (superposiciones > 0) {
                throw new BusinessException("Ya existe una reserva que se cruza con este horario");
            }
        }

        EstadoReserva estadoPendiente = estadoReservaRepository.findByNombreIgnoreCase("PENDIENTE_APROBACION")
                .orElseThrow(() -> new BusinessException("Estado PENDIENTE_APROBACION no encontrado"));

        auditInterceptor.setUsuarioActual();
        Reserva reserva = reservaMapper.toEntity(request);
        reserva.setArea(area);
        reserva.setPersona(persona);
        reserva.setEstado(estadoPendiente);
        
        reserva = reservaRepository.save(reserva);
        log.info("Reserva creada: id={}, area={}", reserva.getId(), area.getNombre());
        return reservaMapper.toResponse(reserva);
    }

    @Override
    @Transactional
    public ReservaResponse update(Long id, ReservaRequest request) {
        if (!request.getHoraFin().isAfter(request.getHoraInicio())) {
            throw new BusinessException("La hora de fin debe ser posterior a la hora de inicio");
        }
        
        LocalDate fechaActual = LocalDate.now();
        if (request.getFecha().isBefore(fechaActual)) {
            throw new BusinessException("No se pueden reprogramar reservas para una fecha en el pasado");
        }
        
        if (request.getFecha().isEqual(fechaActual) && request.getHoraInicio().isBefore(java.time.LocalTime.now())) {
            throw new BusinessException("La hora de inicio de la reserva no puede estar en el pasado");
        }

        Reserva reserva = reservaRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", "id", id));

        if (Boolean.TRUE.equals(request.getBloqueaHorario())) {
            long superposiciones = reservaRepository.countSuperposicionesExcluyendo(
                    request.getAreaId(), request.getFecha(), request.getHoraInicio(), request.getHoraFin(), id);
            if (superposiciones > 0) {
                throw new BusinessException("El horario actualizado se cruza con otra reserva");
            }
        }

        auditInterceptor.setUsuarioActual();
        reservaMapper.updateEntityFromRequest(request, reserva);
        
        if (request.getAreaId() != null && !reserva.getArea().getId().equals(request.getAreaId())) {
            AreaComun area = areaComunRepository.findById(request.getAreaId())
                    .orElseThrow(() -> new ResourceNotFoundException("AreaComun", "id", request.getAreaId()));
            reserva.setArea(area);
        }

        if (request.getPersonaId() != null && !reserva.getPersona().getId().equals(request.getPersonaId())) {
            Persona persona = personaRepository.findById(request.getPersonaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", request.getPersonaId()));
            reserva.setPersona(persona);
        }

        reserva = reservaRepository.save(reserva);
        log.info("Reserva actualizada: id={}", reserva.getId());
        return reservaMapper.toResponse(reserva);
    }

    @Override
    @Transactional
    public ReservaResponse cambiarEstado(Long id, Long estadoId, Long usuarioAprobadorId) {
        Reserva reserva = reservaRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", "id", id));

        EstadoReserva nuevoEstado = estadoReservaRepository.findById(estadoId)
                .orElseThrow(() -> new ResourceNotFoundException("EstadoReserva", "id", estadoId));

        Usuario aprobador = null;
        if (usuarioAprobadorId != null) {
            aprobador = usuarioRepository.findById(usuarioAprobadorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioAprobadorId));
        }

        auditInterceptor.setUsuarioActual();
        reserva.setEstado(nuevoEstado);
        reserva.setUsuarioAprobador(aprobador);
        
        reserva = reservaRepository.save(reserva);
        log.info("Estado de reserva cambiado: id={}, nuevoEstado={}", reserva.getId(), nuevoEstado.getNombre());
        return reservaMapper.toResponse(reserva);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", "id", id));
        
        auditInterceptor.setUsuarioActual();
        reservaRepository.delete(reserva);
        log.info("Reserva eliminada: id={}", id);
    }
}
