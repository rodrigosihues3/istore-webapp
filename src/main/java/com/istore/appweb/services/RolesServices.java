package com.istore.appweb.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.istore.appweb.DTO.roles.RolAgregarDTO;
import com.istore.appweb.DTO.roles.RolEditarDTO;
import com.istore.appweb.configs.Utilidades;
import com.istore.appweb.entities.Roles;
import com.istore.appweb.repositories.RolesRepository;

@Service
public class RolesServices {

  @Autowired
  private RolesRepository repositorio;

  public List<Roles> getRoles() {
    return repositorio.findAll(Sort.by(Sort.Direction.DESC, "idRol"));
  }

  public Roles getRolById(Integer id) {
    return repositorio.findById(id).get();
  }

  public Roles createRol(Roles rol) {
    rol = mapearYNormalizar(rol.getNombre(), rol.getNivel());

    return repositorio.save(rol);
  }

  public Roles updateRol(Roles rol) {
    Roles rolExistente = getRolById(rol.getIdRol());

    rolExistente.setNombre(Utilidades.normalizarTexto(rol.getNombre()));
    rolExistente.setNivel(rol.getNivel());
    rolExistente.setFechaCreacion(LocalDateTime.now());

    return repositorio.save(rol);
  }

  public void deleteRol(Integer id) {
    repositorio.deleteById(id);
  }

  // OTRAS FUNCIONES

  public Roles getByNombre(String nombre) {
    return repositorio.findByNombre(nombre);
  }

  public Roles createRol(RolAgregarDTO rolDto) {
    if (repositorio.existsByNombre(rolDto.getNombre().trim().toUpperCase())) {
      throw new IllegalArgumentException("nombre:Este nombre de rol ya existe, intente con otro.");
    }
    if (repositorio.existsByNivel(rolDto.getNivel())) {
      throw new IllegalArgumentException("nivel:Este nivel de rol ya existe, intente con otro.");
    }

    Roles rol = mapearYNormalizar(rolDto.getNombre(), rolDto.getNivel());

    return repositorio.save(rol);
  }

  public Roles updateRol(RolEditarDTO rolDto) {
    Roles rolExistente = getRolById(rolDto.getIdRol());

    if (repositorio.existsByNombre(rolDto.getNombre().trim().toUpperCase())
        && !rolExistente.getNombre().equals(rolDto.getNombre().trim().toUpperCase())) {
      throw new IllegalArgumentException("nombre:El nuevo nombre de rol ya existe, intente con otro.");
    }
    if (repositorio.existsByNivel(rolDto.getNivel())
        && !Objects.equals(rolExistente.getNivel(), rolDto.getNivel())) {
      throw new IllegalArgumentException("nivel:El nuevo nivel de rol ingresado ya existe, intente con otro.");
    }

    rolExistente.setNombre(Utilidades.normalizarTexto(rolDto.getNombre()));
    rolExistente.setNivel(rolDto.getNivel());
    rolExistente.setFechaCreacion(LocalDateTime.now());

    return repositorio.save(rolExistente);
  }

  public Roles mapearYNormalizar(String nombre, Integer nivel) {
    Roles rol = new Roles();

    rol.setNombre(Utilidades.normalizarTexto(nombre));
    rol.setNivel(nivel);

    return rol;
  }

}
