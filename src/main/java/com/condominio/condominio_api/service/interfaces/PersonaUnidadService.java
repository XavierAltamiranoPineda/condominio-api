package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.PersonaUnidadRequest;
import com.condominio.condominio_api.dto.response.PersonaUnidadResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface PersonaUnidadService {

    @Transactional(readOnly = true)
    PersonaUnidadResponse findById(Long id);

    @Transactional(readOnly = true)
    Page<PersonaUnidadResponse> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    Page<PersonaUnidadResponse> findByPersonaId(Long personaId, Pageable pageable);

    @Transactional(readOnly = true)
    Page<PersonaUnidadResponse> findByUnidadId(Long unidadId, Pageable pageable);

    @Transactional
    PersonaUnidadResponse create(PersonaUnidadRequest request);

    @Transactional
    PersonaUnidadResponse update(Long id, PersonaUnidadRequest request);

    @Transactional
    void delete(Long id);
}
