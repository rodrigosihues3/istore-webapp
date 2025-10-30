package com.istore.appweb.controllers.administrador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.istore.appweb.DTO.usuarios.UsuarioAgregarDTO;
import com.istore.appweb.DTO.usuarios.UsuarioEditarDTO;
import com.istore.appweb.DTO.usuarios.UsuarioEliminarDTO;
import com.istore.appweb.entities.Roles;
import com.istore.appweb.services.RolesServices;
import com.istore.appweb.services.UsuariosServices;

import jakarta.validation.Valid;

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
  public String listarTodo(Model model, Authentication auth) {
    prepararVistaUsuarios(model, auth);

    return VISTA_LISTAR;
  }

  @PostMapping("/agregar")
  public String agregar(@Valid @ModelAttribute("usuarioAgregarDto") UsuarioAgregarDTO usuarioAgregarDto,
      BindingResult result,
      Model model,
      Authentication auth) {
    if (result.hasErrors()) {
      model.addAttribute("usuarioAgregarDto", usuarioAgregarDto);
      prepararVistaUsuarios(model, auth);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_LISTAR;
    }

    try {
      servicio.createUsuario(usuarioAgregarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      model.addAttribute("usuarioAgregarDto", usuarioAgregarDto);
      prepararVistaUsuarios(model, auth);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_LISTAR;
    }

    return REDIRECCIONAR;
  }

  @PostMapping("/editar")
  public String editar(@Valid @ModelAttribute("usuarioEditarDto") UsuarioEditarDTO usuarioEditarDto,
      BindingResult result,
      Model model,
      Authentication auth) {
    if (result.hasErrors()) {
      model.addAttribute("usuarioEditarDto", usuarioEditarDto);
      prepararVistaUsuarios(model, auth);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_LISTAR;
    }

    try {
      servicio.updateUsuario(usuarioEditarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      model.addAttribute("usuarioEditarDto", usuarioEditarDto);
      prepararVistaUsuarios(model, auth);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_LISTAR;
    }

    return REDIRECCIONAR;
  }

  @PostMapping("/eliminar")
  public String eliminar(@ModelAttribute UsuarioEliminarDTO usuarioEliminarDto) {
    servicio.deleteUsuario(usuarioEliminarDto.getIdUsuario());

    return REDIRECCIONAR;
  }

  private void prepararVistaUsuarios(Model model, Authentication auth) {
    if (!model.containsAttribute("usuarioAgregarDto")) {
      model.addAttribute("usuarioAgregarDto", new UsuarioAgregarDTO());
    }
    if (!model.containsAttribute("usuarioEditarDto")) {
      model.addAttribute("usuarioEditarDto", new UsuarioEditarDTO());
    }

    // Obtener usuario actual
    var usuarioActualOpt = servicio.getUsuarioByNombreUsuario(auth.getName());

    // Si existe, filtramos roles por nivel
    if (usuarioActualOpt.isPresent()) {
      var usuarioActual = usuarioActualOpt.get();
      List<Roles> rolesDisponibles = servicioRoles.getRoles()
          .stream()
          .filter(rol -> rol.getNivel() <= usuarioActual.getRol().getNivel())
          .toList();

      model.addAttribute("usuarioActual", usuarioActual);
      model.addAttribute("roles", rolesDisponibles);
    } else {
      // Si no hay usuario logueado, evita NullPointerException
      model.addAttribute("roles", servicioRoles.getRoles());
    }

    model.addAttribute("usuarios", servicio.getUsuarios());
  }

}
