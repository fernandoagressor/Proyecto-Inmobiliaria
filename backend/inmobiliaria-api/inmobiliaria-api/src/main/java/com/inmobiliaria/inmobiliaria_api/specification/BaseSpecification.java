package com.inmobiliaria.inmobiliaria_api.specification;

import org.springframework.data.jpa.domain.Specification;

public final class BaseSpecification {

    private BaseSpecification() {
    }

    public static <T> Specification<T> booleanEsVerdadero(String atributo) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get(atributo));
    }

    public static <T> Specification<T> joinTextoContiene(
            String relacion,
            String atributo,
            String valor) {

        return (root, query, criteriaBuilder) -> {

            if (valor == null || valor.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            var join = root.join(relacion);

            return criteriaBuilder.like(
                    criteriaBuilder.lower(join.get(atributo)),
                    "%" + valor.trim().toLowerCase() + "%"
            );
        };
    }

    public static <T> Specification<T> joinTextoIgual(
            String relacion,
            String atributo,
            String valor) {

        return (root, query, criteriaBuilder) -> {

            if (valor == null || valor.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            var join = root.join(relacion);

            return criteriaBuilder.equal(
                    criteriaBuilder.lower(join.get(atributo)),
                    valor.trim().toLowerCase()
            );
        };
    }
}