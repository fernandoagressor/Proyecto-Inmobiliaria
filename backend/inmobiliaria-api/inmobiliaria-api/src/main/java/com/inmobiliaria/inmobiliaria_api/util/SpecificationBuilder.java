package com.inmobiliaria.inmobiliaria_api.util;

import org.springframework.data.jpa.domain.Specification;

public class SpecificationBuilder<T> {

    private Specification<T> specification;

    private SpecificationBuilder() {
    }

    public static <T> SpecificationBuilder<T> builder() {
        return new SpecificationBuilder<>();
    }

    public SpecificationBuilder<T> and(Specification<T> nuevaSpecification) {

        if (nuevaSpecification == null) {
            return this;
        }

        if (specification == null) {
            specification = nuevaSpecification;
        } else {
            specification = specification.and(nuevaSpecification);
        }

        return this;
    }

    public Specification<T> build() {
        return specification;
    }
}