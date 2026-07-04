package com.condominio.condominio_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/** Respuesta de Rol incluyendo sus permisos — usado solo en el endpoint de detalle. */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolDetalleResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private List<PermisoResponse> permisos;
}
