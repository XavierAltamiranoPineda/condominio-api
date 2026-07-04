package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.RolRequest;
import com.condominio.condominio_api.dto.response.RolDetalleResponse;
import com.condominio.condominio_api.dto.response.RolResponse;
import com.condominio.condominio_api.entity.Permiso;
import com.condominio.condominio_api.entity.Rol;
import com.condominio.condominio_api.entity.RolPermiso;
import com.condominio.condominio_api.exception.BusinessException;
import com.condominio.condominio_api.exception.ResourceAlreadyExistsException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.RolMapper;
import com.condominio.condominio_api.repository.PermisoRepository;
import com.condominio.condominio_api.repository.RolPermisoRepository;
import com.condominio.condominio_api.repository.RolRepository;
import com.condominio.condominio_api.service.interfaces.RolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;
    private final RolPermisoRepository rolPermisoRepository;
    private final RolMapper rolMapper;
    private final PostgresAuditInterceptor auditInterceptor;

    @Override
    public List<RolResponse> findAll() {
        return rolRepository.findAllOrdered().stream()
                .map(rolMapper::toResponse)
                .toList();
    }

    @Override
    public RolDetalleResponse findById(Long id) {
        Rol rol = rolRepository.findByIdWithPermisos(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", id));
        return rolMapper.toDetalleResponse(rol);
    }

    @Override
    public RolResponse create(RolRequest request) {
        if (rolRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new ResourceAlreadyExistsException("Rol", "nombre", request.getNombre());
        }
        auditInterceptor.setUsuarioActual();
        Rol rol = rolMapper.toEntity(request);
        Rol saved = rolRepository.save(rol);
        log.info("Rol creado: id={}, nombre={}", saved.getId(), saved.getNombre());
        return rolMapper.toResponse(saved);
    }

    @Override
    public RolResponse update(Long id, RolRequest request) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", id));

        if (!rol.getNombre().equalsIgnoreCase(request.getNombre())
                && rolRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new ResourceAlreadyExistsException("Rol", "nombre", request.getNombre());
        }
        auditInterceptor.setUsuarioActual();
        rolMapper.updateFromRequest(request, rol);
        Rol saved = rolRepository.save(rol);
        log.info("Rol actualizado: id={}, nombre={}", saved.getId(), saved.getNombre());
        return rolMapper.toResponse(saved);
    }

    @Override
    public void assignPermiso(Long rolId, Long permisoId) {
        if (rolPermisoRepository.existsByIdRolIdAndIdPermisoId(rolId, permisoId)) {
            throw new ResourceAlreadyExistsException("RolPermiso", "rolId+permisoId", rolId + "+" + permisoId);
        }
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", rolId));
        Permiso permiso = permisoRepository.findById(permisoId)
                .orElseThrow(() -> new ResourceNotFoundException("Permiso", "id", permisoId));

        auditInterceptor.setUsuarioActual();
        rolPermisoRepository.save(new RolPermiso(rol, permiso));
        log.info("Permiso {} asignado al rol {}", permisoId, rolId);
    }

    @Override
    public void revokePermiso(Long rolId, Long permisoId) {
        if (!rolPermisoRepository.existsByIdRolIdAndIdPermisoId(rolId, permisoId)) {
            throw new ResourceNotFoundException("RolPermiso", "rolId+permisoId", rolId + "+" + permisoId);
        }
        auditInterceptor.setUsuarioActual();
        rolPermisoRepository.deleteByRolIdAndPermisoId(rolId, permisoId);
        log.info("Permiso {} revocado del rol {}", permisoId, rolId);
    }
}
