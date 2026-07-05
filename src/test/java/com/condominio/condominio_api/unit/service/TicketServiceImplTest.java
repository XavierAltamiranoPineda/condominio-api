package com.condominio.condominio_api.unit.service;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.TicketRequest;
import com.condominio.condominio_api.dto.response.TicketResponse;
import com.condominio.condominio_api.entity.EstadoTicket;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.entity.Ticket;
import com.condominio.condominio_api.entity.Unidad;
import com.condominio.condominio_api.mapper.HistorialTicketMapper;
import com.condominio.condominio_api.mapper.TicketMapper;
import com.condominio.condominio_api.repository.*;
import com.condominio.condominio_api.service.impl.TicketServiceImpl;
import com.condominio.condominio_api.service.interfaces.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock private TicketRepository ticketRepository;
    @Mock private PersonaRepository personaRepository;
    @Mock private UnidadRepository unidadRepository;
    @Mock private CategoriaRepository categoriaRepository;
    @Mock private EstadoTicketRepository estadoTicketRepository;
    @Mock private HistorialTicketRepository historialTicketRepository;
    @Mock private ArchivoRepository archivoRepository;
    @Mock private TicketMapper ticketMapper;
    @Mock private HistorialTicketMapper historialTicketMapper;
    @Mock private PostgresAuditInterceptor auditInterceptor;
    @Mock private StorageService storageService;
    @Mock private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private Ticket ticket;
    private TicketRequest request;
    private TicketResponse response;
    private Persona persona;
    private Unidad unidad;
    private EstadoTicket estado;

    @BeforeEach
    void setUp() {
        persona = new Persona();
        persona.setId(1L);

        unidad = new Unidad();
        unidad.setId(1L);

        estado = new EstadoTicket();
        estado.setId(1L);
        estado.setNombre("ABIERTO");

        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitulo("Fuga de agua");
        ticket.setPrioridad(Ticket.PrioridadTicket.ALTA);
        ticket.setEstadoActual(estado);

        request = new TicketRequest();
        request.setTitulo("Fuga de agua");
        request.setPrioridad("ALTA");
        request.setDescripcion("Hay una fuga de agua en el baño");

        response = TicketResponse.builder()
                .id(1L)
                .titulo("Fuga de agua")
                .estadoActualNombre("ABIERTO")
                .prioridad("ALTA")
                .build();
    }

    @Test
    @DisplayName("✓ create: debe crear el ticket, guardar el historial y disparar el evento")
    void shouldCreateTicket() {
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));
        when(unidadRepository.findById(1L)).thenReturn(Optional.of(unidad));
        when(estadoTicketRepository.findByNombreIgnoreCase("ABIERTO")).thenReturn(Optional.of(estado));
        when(estadoTicketRepository.findById(1L)).thenReturn(Optional.of(estado));
        when(ticketMapper.toEntity(request)).thenReturn(ticket);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(ticketMapper.toResponse(ticket)).thenReturn(response);

        TicketResponse result = ticketService.create(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitulo()).isEqualTo("Fuga de agua");

        verify(ticketRepository).save(any(Ticket.class));
        verify(historialTicketRepository).save(any());
        verify(eventPublisher).publishEvent(any());
    }
}
