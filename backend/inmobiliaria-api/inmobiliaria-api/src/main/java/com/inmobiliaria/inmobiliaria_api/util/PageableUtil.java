package com.inmobiliaria.inmobiliaria_api.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtil {

    private PageableUtil() {
        // Evita instanciar la clase
    }

    public static Pageable crear(
            int page,
            int size,
            String sortBy,
            String direction) {

        // Evita páginas negativas
        page = Math.max(page, 0);

        // Tamaño mínimo 1
        size = Math.max(size, 1);

        // Tamaño máximo 100
        size = Math.min(size, 100);

        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        return PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy)
        );
    }

}