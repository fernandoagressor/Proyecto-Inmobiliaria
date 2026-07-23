package com.inmobiliaria.inmobiliaria_api.service;

import com.inmobiliaria.inmobiliaria_api.entity.Rol;
import java.util.List;

public interface RolService {

    Rol guardar(Rol rol);
    List<Rol> listar();
    Rol buscarPorId(Long id);
    Rol actualizar(Long id, Rol rol);
    void eliminar(Long id);
}
