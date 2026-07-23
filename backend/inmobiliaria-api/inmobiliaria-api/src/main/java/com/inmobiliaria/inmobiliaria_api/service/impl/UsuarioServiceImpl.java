package com.inmobiliaria.inmobiliaria_api.service.impl;

import com.inmobiliaria.inmobiliaria_api.dto.mapper.UsuarioMapper;
import com.inmobiliaria.inmobiliaria_api.dto.request.UsuarioRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.UsuarioResponse;
import com.inmobiliaria.inmobiliaria_api.entity.Persona;
import com.inmobiliaria.inmobiliaria_api.entity.Rol;
import com.inmobiliaria.inmobiliaria_api.entity.Usuario;
import com.inmobiliaria.inmobiliaria_api.repository.PersonaRepository;
import com.inmobiliaria.inmobiliaria_api.repository.RolRepository;
import com.inmobiliaria.inmobiliaria_api.repository.UsuarioRepository;
import com.inmobiliaria.inmobiliaria_api.service.UsuarioService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(
            UsuarioRepository usuarioRepository,
            PersonaRepository personaRepository,
            RolRepository rolRepository,
            UsuarioMapper usuarioMapper,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
        this.rolRepository = rolRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsuarioResponse guardar(UsuarioRequest request) {

        Persona persona = personaRepository.findById(request.getIdPersona())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada."));

        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado."));

        Usuario usuario = new Usuario();

        usuario.setCorreo(request.getCorreo());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setActivo(true);
        usuario.setPersona(persona);
        usuario.setRol(rol);

        return usuarioMapper.toResponse(
                usuarioRepository.save(usuario)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UsuarioResponse> listar(Pageable pageable) {

        Page<UsuarioResponse> pagina = usuarioRepository
                .findByActivoTrue(pageable)
                .map(usuarioMapper::toResponse);

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
    public UsuarioResponse buscarPorId(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        return usuarioMapper.toResponse(usuario);

    }

    @Override
    public UsuarioResponse actualizar(Long id, UsuarioRequest request) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        Persona persona = personaRepository.findById(request.getIdPersona())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada."));

        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado."));

        usuario.setCorreo(request.getCorreo());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setPersona(persona);
        usuario.setRol(rol);

        return usuarioMapper.toResponse(usuarioRepository.save(usuario));

    }

    @Override
    public void eliminar(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        usuario.setActivo(false);

        usuarioRepository.save(usuario);

    }

}