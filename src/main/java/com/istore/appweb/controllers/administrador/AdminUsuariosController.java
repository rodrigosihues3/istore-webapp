package com.istore.appweb.controllers.administrador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.istore.appweb.DTO.usuarios.UsuarioAgregarDTO;
import com.istore.appweb.DTO.usuarios.UsuarioEditarDTO;
import com.istore.appweb.DTO.usuarios.UsuarioEliminarDTO;
import com.istore.appweb.entities.Roles;
import com.istore.appweb.entities.Usuarios;
import com.istore.appweb.services.RolesServices;
import com.istore.appweb.services.UsuariosServices;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuariosController {

  private final String CARPETA_BASE = "tablasBD/";
  private final String VISTA_LISTAR = CARPETA_BASE + "usuarios";
  private final String REDIRECCIONAR = "redirect:/admin/usuarios";

  @Autowired
  private UsuariosServices servicio;

  @Autowired
  private RolesServices servicioRoles;

  @GetMapping
  public String listarTodo(Model model) {
    List<Usuarios> usuarios = servicio.getUsuarios();
    List<Roles> roles = servicioRoles.getRoles();

    model.addAttribute("usuarios", usuarios);
    model.addAttribute("roles", roles);

    return VISTA_LISTAR;
  }

  @PostMapping("/agregar")
  public String agregar(@ModelAttribute UsuarioAgregarDTO usuarioDTO) {
    Usuarios usuario = new Usuarios();

    usuario.setNombres(usuarioDTO.getNombres().toUpperCase());
    usuario.setApellidos(usuarioDTO.getApellidos().toUpperCase());
    usuario.setDireccion(usuarioDTO.getDireccion().toUpperCase());
    usuario.setEmail(usuarioDTO.getEmail().toUpperCase());
    usuario.setNombreUsuario(usuarioDTO.getNombreUsuario().toUpperCase());
    usuario.setPassword(usuarioDTO.getPassword());
    usuario.setDni(usuarioDTO.getDni());
    usuario.setTelefono(usuarioDTO.getTelefono());
    usuario.setRol(servicioRoles.getRolById(usuarioDTO.getIdRol()));

    servicio.createUsuario(usuario);

    return REDIRECCIONAR;
  }

  @PostMapping("/editar")
  public String editar(@ModelAttribute UsuarioEditarDTO usuarioDto) {    

    servicio.updateUsuario(usuarioDto);

    return REDIRECCIONAR;
  }

  @PostMapping("/eliminar")
  public String eliminar(@ModelAttribute UsuarioEliminarDTO usuarioDto) {
    servicio.deleteUsuario(usuarioDto.getIdUsuario());

    return REDIRECCIONAR;
  }

}
