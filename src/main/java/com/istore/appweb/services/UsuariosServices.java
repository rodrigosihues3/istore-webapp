package com.istore.appweb.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.istore.appweb.entities.Usuarios;
import com.istore.appweb.repositories.UsuariosRepository;

@Service
public class UsuariosServices {

  @Autowired
  private UsuariosRepository repositorio;

  public List<Usuarios> getUsuarios() {
    return repositorio.findAll().reversed();
  }

  public Usuarios getUsuarioById(Integer id) {
    return repositorio.findById(id).get();
  }

  public Usuarios createUsuario(Usuarios usuario) {
    return repositorio.save(usuario);
  }

  public Usuarios updateUsuario(Usuarios usuario) {
    return repositorio.save(usuario);
  }

  public void deleteUsuario(Integer id) {
    repositorio.deleteById(id);
  }

}
