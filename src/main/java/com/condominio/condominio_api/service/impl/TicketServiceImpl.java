package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.TicketRequest;
import com.condominio.condominio_api.dto.response.HistorialTicketResponse;
import com.condominio.condominio_api.dto.response.TicketResponse;
import com.condominio.condominio_api.entity.*;
import com.condominio.condominio_api.exception.BusinessException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.HistorialTicketMapper;
import com.condominio.condominio_api.mapper.TicketMapper;
import com.condominio.condominio_api.repository.*;
import com.condominio.condominio_api.service.interfaces.StorageService;
import com.condominio.condominio_api.service.interfaces.TicketService;
import com.condominio.condominio_api.event.TicketEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final PersonaRepository personaRepository;
    private final UnidadRepository unidadRepository;
    private final CategoriaRepository categoriaRepository;
    private final EstadoTicketRepository estadoTicketRepository;
    private final HistorialTicketRepository historialTicketRepository;
    private final ArchivoRepository archivoRepository;
    private final TicketMapper ticketMapper;
    private final HistorialTicketMapper historialTicketMapper;
    private final PostgresAuditInterceptor auditInterceptor;
    private final StorageService storageService;
    private final ApplicationEventPublisher eventPublisher;
    private final UsuarioRepository usuarioRepository;
    private final PersonaUnidadRepository personaUnidadRepository;

    @Override
    public TicketResponse findById(Long id) {
        return ticketRepository.findByIdWithDetails(id)
                .map(ticketMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", id));
    }

    @Override
    public Page<TicketResponse> findAll(Pageable pageable) {
        return ticketRepository.findAllWithDetails(pageable)
                .map(ticketMapper::toResponse);
    }

    @Override
    public Page<TicketResponse> findByCondominioId(Long condominioId, Pageable pageable) {
        return ticketRepository.findByCondominioIdWithDetails(condominioId, pageable)
                .map(ticketMapper::toResponse);
    }

    @Override
    @Transactional
    public TicketResponse create(TicketRequest request) {
        return createWithArchivos(request, List.of());
    }

    @Override
    @Transactional
    public TicketResponse createWithArchivos(TicketRequest request, List<MultipartFile> files) {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof com.condominio.condominio_api.security.CustomUserDetails)) {
            throw new BusinessException("Usuario no autenticado");
        }
        Long usuarioId = ((com.condominio.condominio_api.security.CustomUserDetails) auth.getPrincipal()).getId();
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));
        Persona persona = usuario.getPersona();
        if (persona == null) {
            throw new BusinessException("El usuario no tiene una persona asociada");
        }

        Page<PersonaUnidad> unidadesPage = personaUnidadRepository.findByPersonaIdWithDetails(persona.getId(), org.springframework.data.domain.PageRequest.of(0, 1));
        if (unidadesPage.isEmpty()) {
            throw new BusinessException("El usuario no tiene unidades asociadas");
        }
        Unidad unidad = unidadesPage.getContent().get(0).getUnidad();

        Persona tecnico = null;
        if (request.getTecnicoId() != null) {
            tecnico = personaRepository.findById(request.getTecnicoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tecnico", "id", request.getTecnicoId()));
        }

        Categoria categoria = null;
        if (request.getCategoriaId() != null) {
            categoria = categoriaRepository.findById(request.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", request.getCategoriaId()));
        }

        Long estadoId = request.getEstadoActualId();
        if (estadoId == null) {
            EstadoTicket estadoAbierto = estadoTicketRepository.findByNombreIgnoreCase("ABIERTO")
                    .orElseThrow(() -> new BusinessException("Estado ABIERTO no configurado en la base de datos"));
            estadoId = estadoAbierto.getId();
        }

        final Long finalEstadoId = estadoId;
        EstadoTicket estado = estadoTicketRepository.findById(finalEstadoId)
                .orElseThrow(() -> new ResourceNotFoundException("EstadoTicket", "id", finalEstadoId));

        auditInterceptor.setUsuarioActual();
        Ticket ticket = ticketMapper.toEntity(request);
        ticket.setPersona(persona);
        ticket.setUnidad(unidad);
        ticket.setTecnico(tecnico);
        ticket.setCategoria(categoria);
        ticket.setEstadoActual(estado);
        ticket.setPrioridad(Ticket.PrioridadTicket.valueOf(request.getPrioridad().toUpperCase()));
        
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    Archivo archivo = procesarArchivo(file);
                    ticket.getArchivos().add(archivo);
                }
            }
        }

        ticket = ticketRepository.save(ticket);
        
        registrarHistorial(ticket, estado, "Ticket creado");
        
        eventPublisher.publishEvent(new TicketEvent(this, ticket, TicketEvent.EventType.CREADO, "Se ha creado el ticket: " + ticket.getTitulo()));

        log.info("Ticket creado: id={}, titulo={}", ticket.getId(), ticket.getTitulo());
        return ticketMapper.toResponse(ticket);
    }

    @Override
    @Transactional
    public TicketResponse update(Long id, TicketRequest request) {
        Ticket ticket = ticketRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", id));

        auditInterceptor.setUsuarioActual();
        
        ticket.setTitulo(request.getTitulo());
        ticket.setDescripcion(request.getDescripcion());
        ticket.setPrioridad(Ticket.PrioridadTicket.valueOf(request.getPrioridad().toUpperCase()));
        
        if (request.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", request.getCategoriaId()));
            ticket.setCategoria(categoria);
        }

        ticket = ticketRepository.save(ticket);
        log.info("Ticket actualizado: id={}", ticket.getId());
        return ticketMapper.toResponse(ticket);
    }

    @Override
    @Transactional
    public TicketResponse updateEstado(Long id, Long nuevoEstadoId, String comentario) {
        Ticket ticket = ticketRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", id));

        EstadoTicket nuevoEstado = estadoTicketRepository.findById(nuevoEstadoId)
                .orElseThrow(() -> new ResourceNotFoundException("EstadoTicket", "id", nuevoEstadoId));

        if (ticket.getEstadoActual().getId().equals(nuevoEstadoId)) {
            throw new BusinessException("El ticket ya se encuentra en el estado solicitado");
        }

        auditInterceptor.setUsuarioActual();
        ticket.setEstadoActual(nuevoEstado);
        
        if ("CERRADO".equalsIgnoreCase(nuevoEstado.getNombre()) || "RESUELTO".equalsIgnoreCase(nuevoEstado.getNombre())) {
            ticket.setFechaCierre(OffsetDateTime.now());
        } else {
            ticket.setFechaCierre(null);
        }

        ticket = ticketRepository.save(ticket);
        
        registrarHistorial(ticket, nuevoEstado, comentario);
        
        eventPublisher.publishEvent(new TicketEvent(this, ticket, TicketEvent.EventType.CAMBIO_ESTADO, 
                "El ticket cambió al estado: " + nuevoEstado.getNombre()));

        log.info("Estado de ticket actualizado: id={}, nuevoEstado={}", ticket.getId(), nuevoEstado.getNombre());
        return ticketMapper.toResponse(ticket);
    }

    @Override
    @Transactional
    public TicketResponse updateTecnico(Long id, Long nuevoTecnicoId) {
        Ticket ticket = ticketRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", id));

        Persona tecnico = personaRepository.findById(nuevoTecnicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Persona (Tecnico)", "id", nuevoTecnicoId));

        auditInterceptor.setUsuarioActual();
        ticket.setTecnico(tecnico);
        ticket = ticketRepository.save(ticket);

        registrarHistorial(ticket, ticket.getEstadoActual(), "Técnico asignado/actualizado: " + tecnico.getNombres());
        
        eventPublisher.publishEvent(new TicketEvent(this, ticket, TicketEvent.EventType.ACTUALIZADO, 
                "Se asignó el técnico: " + tecnico.getNombres() + " al ticket."));

        log.info("Técnico asignado a ticket: id={}, tecnico={}", ticket.getId(), tecnico.getNombres());
        return ticketMapper.toResponse(ticket);
    }

    @Override
    @Transactional
    public TicketResponse attachFile(Long id, MultipartFile file) {
        Ticket ticket = ticketRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", id));

        auditInterceptor.setUsuarioActual();
        Archivo archivo = procesarArchivo(file);
        ticket.getArchivos().add(archivo);
        ticket = ticketRepository.save(ticket);
        
        return ticketMapper.toResponse(ticket);
    }

    @Override
    public void delete(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", id));

        auditInterceptor.setUsuarioActual();
        ticketRepository.delete(ticket);

        log.info("Ticket eliminado: id={}", id);
    }

    @Override
    public List<HistorialTicketResponse> getHistorial(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ticket", "id", id);
        }
        
        List<HistorialTicket> historial = historialTicketRepository.findByTicketIdOrderByFechaDesc(id);
        return historialTicketMapper.toResponseList(historial);
    }
    
    private Archivo procesarArchivo(MultipartFile file) {
        String filename = storageService.store(file);
        Archivo archivo = new Archivo();
        archivo.setNombre(file.getOriginalFilename());
        archivo.setRuta(filename);
        archivo.setTipo("TICKET_ADJUNTO");
        archivo.setMimeType(file.getContentType());
        archivo.setTamano(file.getSize());
        return archivoRepository.save(archivo);
    }
    
    private void registrarHistorial(Ticket ticket, EstadoTicket estado, String comentario) {
        Long usuarioId = 1L; // Fallback
        
        HistorialTicket historial = new HistorialTicket();
        historial.setTicket(ticket);
        historial.setEstado(estado);
        Usuario user = new Usuario();
        user.setId(usuarioId);
        historial.setUsuario(user);
        
        historial.setComentario(comentario != null ? comentario : "Cambio de estado a " + estado.getNombre());
        historialTicketRepository.save(historial);
    }
}
