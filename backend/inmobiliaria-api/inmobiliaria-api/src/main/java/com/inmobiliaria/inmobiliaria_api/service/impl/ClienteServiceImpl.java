package com.inmobiliaria.inmobiliaria_api.service.impl;

import com.inmobiliaria.inmobiliaria_api.dto.filter.ClienteFiltro;
import com.inmobiliaria.inmobiliaria_api.dto.mapper.ClienteMapper;
import com.inmobiliaria.inmobiliaria_api.dto.request.ClienteRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.ClienteResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;
import com.inmobiliaria.inmobiliaria_api.entity.Cliente;
import com.inmobiliaria.inmobiliaria_api.entity.Persona;
import com.inmobiliaria.inmobiliaria_api.exception.ResourceAlreadyExistsException;
import com.inmobiliaria.inmobiliaria_api.exception.ResourceNotFoundException;
import com.inmobiliaria.inmobiliaria_api.repository.ClienteRepository;
import com.inmobiliaria.inmobiliaria_api.repository.PersonaRepository;
import com.inmobiliaria.inmobiliaria_api.service.ClienteService;
import com.inmobiliaria.inmobiliaria_api.specification.ClienteSpecification;
import com.inmobiliaria.inmobiliaria_api.util.SpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;
import com.inmobiliaria.inmobiliaria_api.dto.request.ClienteRegistroRequest;
import com.inmobiliaria.inmobiliaria_api.dto.request.ClienteActualizacionRequest;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final PersonaRepository personaRepository;
    private final ClienteMapper clienteMapper;

    @Override
    public ClienteResponse guardar(ClienteRequest request) {

        Persona persona = personaRepository.findById(request.getIdPersona())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));

        Cliente cliente = clienteMapper.toEntity(request);

        cliente.setPersona(persona);
        cliente.setActivo(true);

        return clienteMapper.toResponse(
                clienteRepository.save(cliente)
        );
    }

    @Override
    @Transactional
    public ClienteResponse registrarCliente(ClienteRegistroRequest request) {

        if (personaRepository.existsByNumeroDocumento(request.getNumeroDocumento())) {
            throw new ResourceAlreadyExistsException(
                    "Ya existe una persona registrada con el número de documento "
                            + request.getNumeroDocumento()
            );
        }

        if (personaRepository.existsByCorreo(request.getCorreo())) {
            throw new ResourceAlreadyExistsException(
                    "Ya existe una persona registrada con el correo "
                            + request.getCorreo()
            );
        }

        Persona persona = new Persona();

        persona.setTipoDocumento(request.getTipoDocumento());
        persona.setNumeroDocumento(request.getNumeroDocumento());
        persona.setNombres(request.getNombres());
        persona.setApellidos(request.getApellidos());
        persona.setTelefono(request.getTelefono());
        persona.setCorreo(request.getCorreo());
        persona.setActivo(true);

        Persona personaGuardada = personaRepository.save(persona);

        Cliente cliente = clienteMapper.toEntity(request);

        cliente.setPersona(personaGuardada);
        cliente.setActivo(true);

        Cliente clienteGuardado = clienteRepository.save(cliente);

        return clienteMapper.toResponse(clienteGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ClienteResponse> listar(
            ClienteFiltro filtro,
            Pageable pageable) {

        Specification<Cliente> specification =
                SpecificationBuilder.<Cliente>builder()
                        .and(ClienteSpecification.activo())
                        .and(ClienteSpecification.nombreContiene(
                                filtro.getNombre()
                        ))
                        .and(ClienteSpecification.correoContiene(
                                filtro.getCorreo()
                        ))
                        .and(ClienteSpecification.tipoDocumentoIgual(
                                filtro.getTipoDocumento()
                        ))
                        .and(ClienteSpecification.numeroDocumentoContiene(
                                filtro.getNumeroDocumento()
                        ))
                        .build();

        Page<ClienteResponse> pagina = clienteRepository
                .findAll(specification, pageable)
                .map(clienteMapper::toResponse);

        return new PageResponse<>(
                pagina.getContent(),
                pagina.getNumber(),
                pagina.getSize(),
                pagina.getTotalElements(),
                pagina.getTotalPages(),
                pagina.isFirst(),
                pagina.isLast()
        );
    }

    @Override
    public ClienteResponse buscarPorId(Long id) {

        Cliente cliente = clienteRepository
                .findByIdClienteAndActivoTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cliente no encontrado"));

        return clienteMapper.toResponse(cliente);
    }

    @Override
    @Transactional
    public ClienteResponse actualizar(
            Long id,
            ClienteActualizacionRequest request) {

        Cliente cliente = clienteRepository
                .findByIdClienteAndActivoTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cliente no encontrado")
                );

        Persona persona = cliente.getPersona();

        if (persona == null) {
            throw new ResourceNotFoundException(
                    "El cliente no tiene una persona asociada"
            );
        }

        if (personaRepository.existsByCorreoAndIdPersonaNot(
                request.getCorreo(),
                persona.getIdPersona()
        )) {
            throw new ResourceAlreadyExistsException(
                    "Ya existe otra persona registrada con el correo "
                            + request.getCorreo()
            );
        }

        persona.setTelefono(request.getTelefono());
        persona.setCorreo(request.getCorreo());

        cliente.setDireccion(request.getDireccion());

        personaRepository.save(persona);

        Cliente clienteActualizado = clienteRepository.save(cliente);

        return clienteMapper.toResponse(clienteActualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        Cliente cliente = clienteRepository
                .findByIdClienteAndActivoTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cliente no encontrado"
                        )
                );

        cliente.setActivo(false);

        clienteRepository.save(cliente);
    }
}