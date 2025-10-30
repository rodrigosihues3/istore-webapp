package com.istore.appweb.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.istore.appweb.DTO.usuarios.ClienteActualizarContrasenaDTO;
import com.istore.appweb.DTO.usuarios.ClienteEditarDTO;
import com.istore.appweb.configs.Utilidades;
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

    if (!clienteExistente.getNombreUsuario().equals(clienteDto.getNombreUsuario().trim())
        && repositorio.existsByNombreUsuario(clienteDto.getNombreUsuario().trim())) {
      throw new IllegalArgumentException(
          "nombreUsuario:Este nuevo nombre de usuario ingresado ya existe, intente con otro.");
    }
    if (!clienteExistente.getEmail().equals(clienteDto.getEmail().trim().toUpperCase())
        && repositorio.existsByEmail(clienteDto.getEmail().toUpperCase())) {
      throw new IllegalArgumentException("email:El nuevo correo electrónico ingresado ya existe, intente con otro.");
    }

    clienteExistente.setNombres(Utilidades.normalizarTexto(clienteDto.getNombres()));
    clienteExistente.setApellidos(Utilidades.normalizarTexto(clienteDto.getApellidos()));
    clienteExistente.setEmail(Utilidades.normalizarTexto(clienteDto.getEmail()));
    clienteExistente.setNombreUsuario(clienteDto.getNombreUsuario().trim());
    clienteExistente.setDni(clienteDto.getDni() != null ? clienteDto.getDni().trim() : null);
    clienteExistente.setTelefono(clienteDto.getTelefono() != null ? clienteDto.getTelefono().trim() : null);
    clienteExistente
        .setDireccion(clienteDto.getDireccion() != null ? Utilidades.normalizarTexto(clienteDto.getDireccion()) : null);
    clienteExistente.setFechaCreacion(LocalDateTime.now());

    return repositorio.save(clienteExistente);
  }

  public Usuarios updateContrasena(ClienteActualizarContrasenaDTO clienteDto) {
    Usuarios clienteExistente = repositorio.findById(clienteDto.getIdUsuario()).get();

    // Validar contraseña actual
    if (!encoder.matches(clienteDto.getPassword(), clienteExistente.getPassword())) {
      throw new IllegalArgumentException("password:La contraseña ingresada es incorrecta.");
    }

    // Validar coincidencia de la nueva contraseña
    if (!clienteDto.getNewPassword().equals(clienteDto.getConfirmPassword())) {
      throw new IllegalArgumentException("La nueva contraseña y su confirmación no coinciden.");
    }

    clienteExistente.setPassword(encoder.encode(clienteDto.getNewPassword()));

    return repositorio.save(clienteExistente);
  }

  public void deleteUsuario(Integer id) {
    repositorio.deleteById(id);
  }

}
