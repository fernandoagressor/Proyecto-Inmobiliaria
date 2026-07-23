package com.inmobiliaria.inmobiliaria_api.service.impl;

import com.inmobiliaria.inmobiliaria_api.entity.Rol;
import com.inmobiliaria.inmobiliaria_api.exception.ResourceNotFoundException;
import com.inmobiliaria.inmobiliaria_api.repository.RolRepository;
import com.inmobiliaria.inmobiliaria_api.service.RolService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RolServiceImpl implements RolService {
    private final RolRepository rolRepository;
    public RolServiceImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }
    @Override
    public Rol guardar(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public List<Rol> listar() {
        return rolRepository.findAll();
    }

    @Override
    public Rol buscarPorId(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException("El rol con ID " + id + " no existe."));
    }

    @Override
    public Rol actualizar(Long id, Rol rol) {
        Rol rolExistente = rolRepository.findById(id).orElse(null);
        if (rolExistente == null) {
            return null;
        }
        rolExistente.setNombre(rol.getNombre());
        return rolRepository.save(rolExistente);
    }

    @Override
    public void eliminar(Long id) {
        Rol rol = rolRepository.findById(id).orElse(null);
        if (rol == null) {
            return;
        }
        rolRepository.delete(rol);
    }
}
