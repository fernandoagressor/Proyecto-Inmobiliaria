package com.inmobiliaria.inmobiliaria_api.specification;

import com.inmobiliaria.inmobiliaria_api.entity.Cliente;
import org.springframework.data.jpa.domain.Specification;

public class ClienteSpecification {

    private ClienteSpecification() {
    }

    public static Specification<Cliente> activo() {
        return BaseSpecification.booleanEsVerdadero("activo");
    }
    public static Specification<Cliente> nombreContiene(String nombre) {

        return (root, query, criteriaBuilder) -> {

            if (nombre == null || nombre.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            var persona = root.join("persona");

            String textoBusqueda = "%" + nombre.trim().toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(persona.get("nombres")),
                            textoBusqueda
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(persona.get("apellidos")),
                            textoBusqueda
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    criteriaBuilder.concat(
                                            criteriaBuilder.concat(
                                                    persona.get("nombres"),
                                                    " "
                                            ),
                                            persona.get("apellidos")
                                    )
                            ),
                            textoBusqueda
                    )
            );
        };
    }
    public static Specification<Cliente> correoContiene(String correo) {
        return BaseSpecification.joinTextoContiene(
                "persona",
                "correo",
                correo
        );
    }
    public static Specification<Cliente> tipoDocumentoIgual(
            String tipoDocumento) {

        return BaseSpecification.joinTextoIgual(
                "persona",
                "tipoDocumento",
                tipoDocumento
        );
    }
    public static Specification<Cliente> numeroDocumentoContiene(
            String numeroDocumento) {

        return BaseSpecification.joinTextoContiene(
                "persona",
                "numeroDocumento",
                numeroDocumento
        );
    }
}