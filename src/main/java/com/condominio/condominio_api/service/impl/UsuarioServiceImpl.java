package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.UsuarioRequest;
import com.condominio.condominio_api.dto.response.UsuarioResponse;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.entity.Rol;
import com.condominio.condominio_api.entity.Usuario;
import com.condominio.condominio_api.entity.UsuarioRol;
import com.condominio.condominio_api.exception.BusinessException;
import com.condominio.condominio_api.exception.ResourceAlreadyExistsException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.UsuarioMapper;
import com.condominio.condominio_api.repository.PersonaRepository;
import com.condominio.condominio_api.repository.RolRepository;
import com.condominio.condominio_api.repository.UsuarioRolRepository;
import com.condominio.condominio_api.repository.UsuarioRepository;
import com.condominio.condominio_api.service.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final PostgresAuditInterceptor auditInterceptor;

    @Override
    public Page<UsuarioResponse> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(usuarioMapper::toResponse);
    }

    @Override
    public UsuarioResponse findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    public UsuarioResponse create(UsuarioRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("Usuario", "username", request.getUsername());
        }
        if (usuarioRepository.existsByPersonaId(request.getIdPersona())) {
            throw new BusinessException("La persona seleccionada ya tiene un usuario de sistema asociado");
        }

        Persona persona = personaRepository.findById(request.getIdPersona())
                .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", request.getIdPersona()));

        auditInterceptor.setUsuarioActual();
        Usuario usuario = usuarioMapper.toEntity(request);
        usuario.setPersona(persona);
        usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        Usuario saved = usuarioRepository.save(usuario);
        log.info("Usuario creado: id={}, username={}", saved.getId(), saved.getUsername());
        return usuarioMapper.toResponse(saved);
    }

    @Override
    public UsuarioResponse update(Long id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        if (!usuario.getUsername().equalsIgnoreCase(request.getUsername())
                && usuarioRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("Usuario", "username", request.getUsername());
        }

        auditInterceptor.setUsuarioActual();
        usuarioMapper.updateFromRequest(request, usuario);
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }
        Usuario saved = usuarioRepository.save(usuario);
        log.info("Usuario actualizado: id={}, username={}", saved.getId(), saved.getUsername());
        return usuarioMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        auditInterceptor.setUsuarioActual();
        usuarioRepository.delete(usuario);
        log.info("Usuario eliminado: id={}, username={}", id, usuario.getUsername());
    }

    @Override
    public void assignRol(Long usuarioId, Long rolId) {
        if (usuarioRolRepository.existsByIdUsuarioIdAndIdRolId(usuarioId, rolId)) {
            throw new ResourceAlreadyExistsException("UsuarioRol", "usuarioId+rolId", usuarioId + "+" + rolId);
        }
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", rolId));

        auditInterceptor.setUsuarioActual();
        usuarioRolRepository.save(new UsuarioRol(usuario, rol));
        log.info("Rol {} asignado al usuario {}", rolId, usuarioId);
    }

    @Override
    public void revokeRol(Long usuarioId, Long rolId) {
        if (!usuarioRolRepository.existsByIdUsuarioIdAndIdRolId(usuarioId, rolId)) {
            throw new ResourceNotFoundException("UsuarioRol", "usuarioId+rolId", usuarioId + "+" + rolId);
        }
        auditInterceptor.setUsuarioActual();
        usuarioRolRepository.deleteByUsuarioIdAndRolId(usuarioId, rolId);
        log.info("Rol {} revocado al usuario {}", rolId, usuarioId);
    }
}
