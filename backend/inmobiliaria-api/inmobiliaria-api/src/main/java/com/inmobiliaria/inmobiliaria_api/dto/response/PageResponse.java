package com.inmobiliaria.inmobiliaria_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {

    private List<T> contenido;

    private int paginaActual;

    private int tamanoPagina;

    private long totalRegistros;

    private int totalPaginas;

    private boolean primera;

    private boolean ultima;

}