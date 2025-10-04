package com.istore.appweb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.istore.appweb.DTO.usuarios.ClienteActualizarContrasenaDTO;
import com.istore.appweb.DTO.usuarios.ClienteEditarDTO;
import com.istore.appweb.entities.Usuarios;
import com.istore.appweb.repositories.UsuariosRepository;

@Service
public class ClientesServices {

  @Autowired
  private UsuariosRepository repositorio;

  @Autowired
  private PasswordEncoder encoder;

  public Usuarios getUsuarioById(Integer id) {
    return repositorio.findById(id).get();
  }

  public Usuarios createUsuario(Usuarios usuario) {
    usuario.setPassword(encoder.encode(usuario.getPassword()));

    return repositorio.save(usuario);
  }

  public Usuarios updateUsuario(ClienteEditarDTO clienteDto) {
    Usuarios clienteExistente = repositorio.findById(clienteDto.getIdUsuario()).get();

    clienteExistente.setNombres(clienteDto.getNombres().toUpperCase());
    clienteExistente.setApellidos(clienteDto.getApellidos().toUpperCase());
    clienteExistente.setEmail(clienteDto.getEmail().toUpperCase());
    clienteExistente.setNombreUsuario(clienteDto.getNombreUsuario().toUpperCase());
    clienteExistente.setDni(clienteDto.getDni());
    clienteExistente.setTelefono(clienteDto.getTelefono());
    clienteExistente.setDireccion(clienteDto.getDireccion().toUpperCase());

    return repositorio.save(clienteExistente);
  }

  public Usuarios updateContrasena(ClienteActualizarContrasenaDTO clienteContrasenaDto) {
    Usuarios clienteExistente = repositorio.findById(clienteContrasenaDto.getIdUsuario()).get();

    clienteExistente.setPassword(encoder.encode(clienteContrasenaDto.getNewPassword()));

    return repositorio.save(clienteExistente);
  }

  public void deleteUsuario(Integer id) {
    repositorio.deleteById(id);
  }

}
