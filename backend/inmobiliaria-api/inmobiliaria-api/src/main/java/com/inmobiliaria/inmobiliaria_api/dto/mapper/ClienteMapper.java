package com.inmobiliaria.inmobiliaria_api.dto.mapper;

import com.inmobiliaria.inmobiliaria_api.dto.request.ClienteRegistroRequest;
import com.inmobiliaria.inmobiliaria_api.dto.request.ClienteRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.ClienteResponse;
import com.inmobiliaria.inmobiliaria_api.entity.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteRequest request) {

        Cliente cliente = new Cliente();

        cliente.setDireccion(request.getDireccion());

        return cliente;
    }

    public ClienteResponse toResponse(Cliente cliente) {

        ClienteResponse response = new ClienteResponse();

        response.setIdCliente(cliente.getIdCliente());
        response.setDireccion(cliente.getDireccion());

        if (cliente.getPersona() != null) {

            response.setIdPersona(cliente.getPersona().getIdPersona());

            response.setNombreCompleto(
                    cliente.getPersona().getNombres() + " " +
                            cliente.getPersona().getApellidos()
            );

            response.setTelefono(
                    cliente.getPersona().getTelefono()
            );

            response.setCorreo(
                    cliente.getPersona().getCorreo()
            );
        }

        return response;
    }
    public Cliente toEntity(ClienteRegistroRequest request) {

        Cliente cliente = new Cliente();

        cliente.setDireccion(request.getDireccion());
        cliente.setActivo(true);

        return cliente;
    }
}