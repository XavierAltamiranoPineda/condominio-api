package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.ReciboRequest;
import com.condominio.condominio_api.dto.response.ReciboResponse;
import com.condominio.condominio_api.entity.Archivo;
import com.condominio.condominio_api.entity.Pago;
import com.condominio.condominio_api.entity.Recibo;
import com.condominio.condominio_api.exception.ResourceAlreadyExistsException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.ReciboMapper;
import com.condominio.condominio_api.repository.ArchivoRepository;
import com.condominio.condominio_api.repository.PagoRepository;
import com.condominio.condominio_api.repository.ReciboRepository;
import com.condominio.condominio_api.service.interfaces.ReciboService;
import com.condominio.condominio_api.service.interfaces.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReciboServiceImpl implements ReciboService {

    private final ReciboRepository reciboRepository;
    private final PagoRepository pagoRepository;
    private final ArchivoRepository archivoRepository;
    private final ReciboMapper reciboMapper;
    private final PostgresAuditInterceptor auditInterceptor;
    private final StorageService storageService;

    @Override
    public ReciboResponse findById(Long id) {
        return reciboRepository.findByIdWithDetails(id)
                .map(reciboMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Recibo", "id", id));
    }

    @Override
    public Page<ReciboResponse> findAll(Pageable pageable) {
        return reciboRepository.findAllWithDetails(pageable)
                .map(reciboMapper::toResponse);
    }

    @Override
    public Page<ReciboResponse> findByPagoId(Long pagoId, Pageable pageable) {
        if (!pagoRepository.existsById(pagoId)) {
            throw new ResourceNotFoundException("Pago", "id", pagoId);
        }
        return reciboRepository.findByPagoIdWithDetails(pagoId, pageable)
                .map(reciboMapper::toResponse);
    }

    @Override
    public ReciboResponse create(ReciboRequest request) {
        if (reciboRepository.existsByNumeroIgnoreCase(request.getNumero())) {
            throw new ResourceAlreadyExistsException("Recibo", "numero", request.getNumero());
        }

        if (reciboRepository.existsByPagoId(request.getPagoId())) {
            throw new ResourceAlreadyExistsException("Recibo", "pagoId", request.getPagoId().toString());
        }

        Pago pago = pagoRepository.findById(request.getPagoId())
                .orElseThrow(() -> new ResourceNotFoundException("Pago", "id", request.getPagoId()));

        Archivo archivo = null;
        if (request.getArchivoId() != null) {
            archivo = archivoRepository.findById(request.getArchivoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Archivo", "id", request.getArchivoId()));
        }

        auditInterceptor.setUsuarioActual();
        Recibo recibo = reciboMapper.toEntity(request);
        recibo.setPago(pago);
        recibo.setArchivo(archivo);
        recibo = reciboRepository.save(recibo);

        log.info("Recibo creado: id={}, numero={}", recibo.getId(), recibo.getNumero());
        return reciboMapper.toResponse(recibo);
    }

    @Override
    public ReciboResponse createWithFile(Long pagoId, String numero, MultipartFile file) {
        String filename = storageService.store(file);
        
        Archivo archivo = new Archivo();
        archivo.setNombre(file.getOriginalFilename());
        archivo.setRuta(filename);
        archivo.setTipo("RECIBO");
        archivo.setMimeType(file.getContentType());
        archivo.setTamano(file.getSize());
        archivo = archivoRepository.save(archivo);

        ReciboRequest request = new ReciboRequest();
        request.setNumero(numero);
        request.setPagoId(pagoId);
        request.setArchivoId(archivo.getId());

        return create(request);
    }

    @Override
    public Resource getArchivoResource(Long reciboId) {
        Recibo recibo = reciboRepository.findByIdWithDetails(reciboId)
                .orElseThrow(() -> new ResourceNotFoundException("Recibo", "id", reciboId));
        
        if (recibo.getArchivo() == null) {
            throw new ResourceNotFoundException("Archivo", "reciboId", reciboId);
        }
        
        return storageService.loadAsResource(recibo.getArchivo().getRuta());
    }

    @Override
    public ReciboResponse update(Long id, ReciboRequest request) {
        Recibo recibo = reciboRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recibo", "id", id));

        if (reciboRepository.existsByNumeroIgnoreCaseAndIdNot(request.getNumero(), id)) {
            throw new ResourceAlreadyExistsException("Recibo", "numero", request.getNumero());
        }

        if (reciboRepository.existsByPagoIdAndIdNot(request.getPagoId(), id)) {
            throw new ResourceAlreadyExistsException("Recibo", "pagoId", request.getPagoId().toString());
        }

        if (!recibo.getPago().getId().equals(request.getPagoId())) {
            Pago pago = pagoRepository.findById(request.getPagoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Pago", "id", request.getPagoId()));
            recibo.setPago(pago);
        }

        if (request.getArchivoId() != null) {
            if (recibo.getArchivo() == null || !recibo.getArchivo().getId().equals(request.getArchivoId())) {
                Archivo archivo = archivoRepository.findById(request.getArchivoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Archivo", "id", request.getArchivoId()));
                recibo.setArchivo(archivo);
            }
        } else {
            recibo.setArchivo(null);
        }

        auditInterceptor.setUsuarioActual();
        reciboMapper.updateEntityFromRequest(request, recibo);
        recibo = reciboRepository.save(recibo);

        log.info("Recibo actualizado: id={}, numero={}", recibo.getId(), recibo.getNumero());
        return reciboMapper.toResponse(recibo);
    }

    @Override
    public void delete(Long id) {
        Recibo recibo = reciboRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recibo", "id", id));

        auditInterceptor.setUsuarioActual();
        reciboRepository.delete(recibo);

        log.info("Recibo eliminado: id={}", id);
    }
}
