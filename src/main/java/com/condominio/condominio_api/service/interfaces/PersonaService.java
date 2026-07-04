package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.PersonaRequest;
import com.condominio.condominio_api.dto.response.PersonaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface PersonaService {

    @Transactional(readOnly = true)
    Page<PersonaResponse> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    PersonaResponse findById(Long id);

    @Transactional
    PersonaResponse create(PersonaRequest request);

    @Transactional
    PersonaResponse update(Long id, PersonaRequest request);

    @Transactional
    void delete(Long id);
}
