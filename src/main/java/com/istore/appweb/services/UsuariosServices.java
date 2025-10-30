package com.istore.appweb.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.istore.appweb.DTO.usuarios.ClienteRegistrarDTO;
import com.istore.appweb.DTO.usuarios.UsuarioAgregarDTO;
import com.istore.appweb.DTO.usuarios.UsuarioEditarDTO;
import com.istore.appweb.configs.Utilidades;
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

  public Usuarios createUsuario(UsuarioAgregarDTO usuarioDTO) {
    if (repositorio.existsByNombreUsuario(usuarioDTO.getNombreUsuario().trim())) {
      throw new IllegalArgumentException("nombreUsuario:Este nombre de usuario ya existe, intente con otro.");
    }
    if (repositorio.existsByEmail(usuarioDTO.getEmail().trim().toUpperCase())) {
      throw new IllegalArgumentException("email:Este correo electrónico ya existe, intente con otro.");
    }

    Usuarios usuario = mapearYNormalizar(
        usuarioDTO.getNombres(),
        usuarioDTO.getApellidos(),
        usuarioDTO.getEmail(),
        usuarioDTO.getNombreUsuario(),
        usuarioDTO.getPassword(),
        usuarioDTO.getDni(),
        usuarioDTO.getTelefono(),
        usuarioDTO.getDireccion());

    usuario.setRol(
        repositorioRoles.findById(usuarioDTO.getIdRol())
            .orElse(repositorioRoles.findByNombre("CLIENTE")));

    return repositorio.save(usuario);
  }

  public Usuarios updateUsuario(UsuarioEditarDTO usuarioDto) {
    Usuarios usuarioExistente = repositorio.findById(usuarioDto.getIdUsuario()).get();

    if (!usuarioExistente.getNombreUsuario().equals(usuarioDto.getNombreUsuario().trim())
        && repositorio.existsByNombreUsuario(usuarioDto.getNombreUsuario().trim())) {
      throw new IllegalArgumentException(
          "nombreUsuario:Este nuevo nombre de usuario ingresado ya existe, intente con otro.");
    }
    if (!usuarioExistente.getEmail().equals(usuarioDto.getEmail().trim().toUpperCase())
        && repositorio.existsByEmail(usuarioDto.getEmail().toUpperCase())) {
      throw new IllegalArgumentException("email:El nuevo correo electrónico ingresado ya existe, intente con otro.");
    }

    usuarioExistente.setNombres(Utilidades.normalizarTexto(usuarioDto.getNombres()));
    usuarioExistente.setApellidos(Utilidades.normalizarTexto(usuarioDto.getApellidos()));
    usuarioExistente.setEmail(Utilidades.normalizarTexto(usuarioDto.getEmail()));
    usuarioExistente.setNombreUsuario(usuarioDto.getNombreUsuario().trim());
    usuarioExistente.setDni(usuarioDto.getDni() != null ? usuarioDto.getDni().trim() : null);
    usuarioExistente.setTelefono(usuarioDto.getTelefono() != null ? usuarioDto.getTelefono().trim() : null);
    usuarioExistente
        .setDireccion(usuarioDto.getDireccion() != null ? Utilidades.normalizarTexto(usuarioDto.getDireccion()) : null);
    usuarioExistente.setRol(repositorioRoles.findById(usuarioDto.getIdRol()).get());
    usuarioExistente.setFechaCreacion(LocalDateTime.now());

    if (usuarioDto.getPassword() != null && !usuarioDto.getPassword().trim().isEmpty()) {
      usuarioExistente.setPassword(encoder.encode(usuarioDto.getPassword()));
    }

    return repositorio.save(usuarioExistente);
  }

  public void deleteUsuario(Integer id) {
    repositorio.deleteById(id);
  }

  // Sobrecarga registrar usuario
  public Usuarios createUsuario(ClienteRegistrarDTO clienteDTO) {
    if (repositorio.existsByNombreUsuario(clienteDTO.getNombreUsuario().trim())) {
      throw new IllegalArgumentException("nombreUsuario:Este nombre de usuario ya existe, intente con otro.");
    }
    if (repositorio.existsByEmail(clienteDTO.getEmail().trim().toUpperCase())) {
      throw new IllegalArgumentException("email:Este correo electrónico ya existe, intente con otro.");
    }

    Usuarios usuario = mapearYNormalizar(clienteDTO.getNombres(),
        clienteDTO.getApellidos(),
        clienteDTO.getEmail(),
        clienteDTO.getNombreUsuario(),
        clienteDTO.getPassword(),
        null, null, null);

    // Los clientes siempre tienen rol "CLIENTE"
    usuario.setRol(repositorioRoles.findByNombre("CLIENTE"));

    return repositorio.save(usuario);
  }

  // Sobrecarga agregar usuario cuando la bd esta vacia
  public Usuarios createUsuario(Usuarios usuario) {
    if (repositorio.existsByNombreUsuario(usuario.getNombreUsuario().trim())) {
      throw new IllegalArgumentException("nombreUsuario:Este nombre de usuario ya existe, intente con otro.");
    }
    if (repositorio.existsByEmail(usuario.getEmail().trim().toUpperCase())) {
      throw new IllegalArgumentException("email:Este correo electrónico ya existe, intente con otro.");
    }

    return repositorio.save(mapearYNormalizar(
        usuario.getNombres(),
        usuario.getApellidos(),
        usuario.getEmail(),
        usuario.getNombreUsuario(),
        usuario.getPassword(),
        usuario.getDni(),
        usuario.getTelefono(),
        usuario.getDireccion()));
  }

  private Usuarios mapearYNormalizar(String nombres, String apellidos, String email,
      String nombreUsuario, String password,
      String dni, String telefono, String direccion) {
    Usuarios usuario = new Usuarios();

    usuario.setNombres(Utilidades.normalizarTexto(nombres));
    usuario.setApellidos(Utilidades.normalizarTexto(apellidos));
    usuario.setEmail(Utilidades.normalizarTexto(email));
    usuario.setNombreUsuario(nombreUsuario.trim());
    usuario.setPassword(encoder.encode(password));

    usuario.setDni(dni != null ? Utilidades.normalizarTexto(dni) : null);
    usuario.setTelefono(telefono != null ? Utilidades.normalizarTexto(telefono) : null);
    usuario.setDireccion(direccion != null ? Utilidades.normalizarTexto(direccion) : null);

    return usuario;
  }
}
