package com.istore.appweb.controllers.administrador;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.istore.appweb.DTO.usuarios.UsuarioEditarDTO;
import com.istore.appweb.DTO.usuarios.UsuarioEliminarDTO;
import com.istore.appweb.entities.Usuarios;
import com.istore.appweb.services.UsuariosServices;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuariosController {

  private final String CARPETA_BASE = "tablasBD/";
  private final String VISTA_LISTAR = CARPETA_BASE + "usuarios";
  private final String REDIRECCIONAR = "redirect:/admin/usuarios";

  @Autowired
  private UsuariosServices servicio;

  @GetMapping
  public String listarTodo(Model model) {
    List<Usuarios> usuarios = servicio.getUsuarios();

    model.addAttribute("usuarios", usuarios);
    model.addAttribute("usuario", new Usuarios());

    return VISTA_LISTAR;
  }

  @PostMapping("/agregar")
  public String agregar(@ModelAttribute Usuarios usuario) {
    usuario.setNombres(usuario.getNombres().toUpperCase());
    usuario.setApellidos(usuario.getApellidos().toUpperCase());
    usuario.setDireccion(usuario.getDireccion().toUpperCase());
    usuario.setEmail(usuario.getEmail().toLowerCase());
    usuario.setNombreUsuario(usuario.getNombreUsuario().toLowerCase());

    servicio.createUsuario(usuario);

    return REDIRECCIONAR;
  }

  @PostMapping("/editar")
  public String editar(@ModelAttribute UsuarioEditarDTO usuarioDto) {
    Usuarios usuarioExistente = servicio.getUsuarioById(usuarioDto.getIdUsuario());

    usuarioExistente.setNombres(usuarioDto.getNombres().toUpperCase());
    usuarioExistente.setApellidos(usuarioDto.getApellidos().toUpperCase());
    usuarioExistente.setEmail(usuarioDto.getEmail().toUpperCase());
    usuarioExistente.setNombreUsuario(usuarioDto.getNombreUsuario().toUpperCase());
    usuarioExistente.setPassword(usuarioDto.getPassword());
    usuarioExistente.setDni(usuarioDto.getDni());
    usuarioExistente.setTelefono(usuarioDto.getTelefono());
    usuarioExistente.setDireccion(usuarioDto.getDireccion().toUpperCase());
    usuarioExistente.setRol(usuarioDto.getRol());

    usuarioExistente.setFechaCreacion(LocalDateTime.now());

    servicio.updateUsuario(usuarioExistente);

    return REDIRECCIONAR;
  }

  @PostMapping("/eliminar")
  public String eliminar(@ModelAttribute UsuarioEliminarDTO usuarioDto) {
    servicio.deleteUsuario(usuarioDto.getIdUsuario());

    return REDIRECCIONAR;
  }

}
