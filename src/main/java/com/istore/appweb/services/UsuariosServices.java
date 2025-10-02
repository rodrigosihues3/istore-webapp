package com.istore.appweb.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.istore.appweb.DTO.usuarios.UsuarioEditarDTO;
import com.istore.appweb.entities.Usuarios;
import com.istore.appweb.repositories.RolesRepository;
import com.istore.appweb.repositories.UsuariosRepository;

@Service
public class UsuariosServices {

  @Autowired
  private UsuariosRepository repositorio;

  @Autowired
  private RolesRepository repositorioRoles;

  @Autowired
  private PasswordEncoder encoder;

  public List<Usuarios> getUsuarios() {
    return repositorio.findAll(Sort.by(Sort.Direction.DESC, "idUsuario"));
  }

  public Usuarios getUsuarioById(Integer id) {
    return repositorio.findById(id).get();
  }

  public Optional<Usuarios> getUsuarioByNombreUsuario(String nombreUsuario) {
    return repositorio.findByNombreUsuario(nombreUsuario);
  }

  public Optional<Usuarios> getUsuarioByEmail(String email) {
    return repositorio.findByEmail(email);
  }

  public Usuarios createUsuario(Usuarios usuario) {
    usuario.setPassword(encoder.encode(usuario.getPassword()));

    return repositorio.save(usuario);
  }

  public Usuarios updateUsuario(UsuarioEditarDTO usuarioDto) {
    Usuarios usuarioExistente = repositorio.findById(usuarioDto.getIdUsuario()).get();

    usuarioExistente.setNombres(usuarioDto.getNombres().toUpperCase());
    usuarioExistente.setApellidos(usuarioDto.getApellidos().toUpperCase());
    usuarioExistente.setEmail(usuarioDto.getEmail().toUpperCase());
    usuarioExistente.setNombreUsuario(usuarioDto.getNombreUsuario().toUpperCase());
    usuarioExistente.setDni(usuarioDto.getDni());
    usuarioExistente.setTelefono(usuarioDto.getTelefono());
    usuarioExistente.setDireccion(usuarioDto.getDireccion().toUpperCase());
    usuarioExistente.setRol(repositorioRoles.findById(usuarioDto.getIdRol()).get());

    usuarioExistente.setFechaCreacion(LocalDateTime.now());

    if (usuarioDto.getPassword() != null && !usuarioDto.getPassword().isBlank()) {
      usuarioExistente.setPassword(encoder.encode(usuarioDto.getPassword()));
    }

    return repositorio.save(usuarioExistente);
  }

  public void deleteUsuario(Integer id) {
    repositorio.deleteById(id);
  }

}
