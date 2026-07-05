package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.TicketRequest;
import com.condominio.condominio_api.dto.response.HistorialTicketResponse;
import com.condominio.condominio_api.dto.response.TicketResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TicketService {
    TicketResponse findById(Long id);
    Page<TicketResponse> findAll(Pageable pageable);
    Page<TicketResponse> findByCondominioId(Long condominioId, Pageable pageable);
    TicketResponse create(TicketRequest request);
    TicketResponse createWithArchivos(TicketRequest request, List<MultipartFile> files);
    TicketResponse update(Long id, TicketRequest request);
    TicketResponse updateEstado(Long id, Long nuevoEstadoId, String comentario);
    TicketResponse updateTecnico(Long id, Long nuevoTecnicoId);
    TicketResponse attachFile(Long id, MultipartFile file);
    void delete(Long id);
    List<HistorialTicketResponse> getHistorial(Long id);
}
