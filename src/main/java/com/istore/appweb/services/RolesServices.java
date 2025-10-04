package com.istore.appweb.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    return repositorio.save(rol);
  }

  public Roles updateRol(Roles rol) {
    return repositorio.save(rol);
  }

  public void deleteRol(Integer id) {
    repositorio.deleteById(id);
  }

  // OTRAS FUNCIONES

  public Roles getByNombre(String nombre) {
    return repositorio.findByNombre(nombre);
  }

}
