package com.istore.appweb.controllers.administrador;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.istore.appweb.DTO.usuarios.*;
import com.istore.appweb.entities.Roles;
import com.istore.appweb.services.*;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuariosController {

  // Ruta del fragmento
  private final String FRAGMENTO = "tablaUsuarios";
  private final String VISTA_FRAGMENTO = "administrador/tablasBD/usuarios :: " + FRAGMENTO;

  @Autowired
  private UsuariosServices servicio;

  @Autowired
  private RolesServices servicioRoles;

  // 1. Endpoint AJAX para el Menú Lateral
  @GetMapping("/tabla")
  public String obtenerTodo(Model model, Authentication auth) {
    prepararVista(model, auth);

    return VISTA_FRAGMENTO;
  }

  // 2. Endpoint "Fallback" (por si alguien entra por URL directa)
  @GetMapping
  public String listarTodo(Model model, Authentication auth) {
    // Redirigimos al dashboard para forzar la estructura
    return "redirect:/admin";
  }

  @PostMapping("/agregar")
  public String agregar(@Valid @ModelAttribute("usuarioAgregarDto") UsuarioAgregarDTO usuarioAgregarDto,
      BindingResult result, Model model, Authentication auth) {

    if (result.hasErrors()) {
      prepararVista(model, auth);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_FRAGMENTO;
    }

    try {
      servicio.createUsuario(usuarioAgregarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2)
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);

      prepararVista(model, auth);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_FRAGMENTO;
    }

    // Si paso las validaciones anteiores, entonces se sobrescribe el objeto que
    // tiene los datos viejos con uno nuevo y limpio
    model.addAttribute("usuarioAgregarDto", new UsuarioAgregarDTO());
    prepararVista(model, auth);

    return VISTA_FRAGMENTO;
  }

  @PostMapping("/editar")
  public String editar(@Valid @ModelAttribute("usuarioEditarDto") UsuarioEditarDTO usuarioEditarDto,
      BindingResult result, Model model, Authentication auth) {

    if (result.hasErrors()) {
      prepararVista(model, auth);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_FRAGMENTO;
    }

    try {
      servicio.updateUsuario(usuarioEditarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2)
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);

      prepararVista(model, auth);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_FRAGMENTO;
    }

    prepararVista(model, auth);

    return VISTA_FRAGMENTO;
  }

  @PostMapping("/eliminar")
  public String eliminar(@ModelAttribute UsuarioEliminarDTO usuarioEliminarDto, Model model, Authentication auth) {
    servicio.deleteUsuario(usuarioEliminarDto.getIdUsuario());
    prepararVista(model, auth);

    return VISTA_FRAGMENTO;
  }

  private void prepararVista(Model model, Authentication auth) {
    if (!model.containsAttribute("usuarioAgregarDto"))
      model.addAttribute("usuarioAgregarDto", new UsuarioAgregarDTO());
    if (!model.containsAttribute("usuarioEditarDto"))
      model.addAttribute("usuarioEditarDto", new UsuarioEditarDTO());

    var usuarioActualOpt = servicio.getUsuarioByNombreUsuario(auth.getName());
    if (usuarioActualOpt.isPresent()) {
      var usuarioActual = usuarioActualOpt.get();
      // Lógica de filtrado de roles por nivel
      List<Roles> rolesDisponibles = servicioRoles.getRoles().stream()
          .filter(rol -> rol.getNivel() <= usuarioActual.getRol().getNivel()).toList();

      model.addAttribute("usuarioActual", usuarioActual);
      model.addAttribute("roles", rolesDisponibles);
    } else {
      model.addAttribute("roles", servicioRoles.getRoles());
    }
    model.addAttribute("usuarios", servicio.getUsuarios());
  }
}