package com.istore.appweb.controllers.administrador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.istore.appweb.DTO.roles.RolAgregarDTO;
import com.istore.appweb.DTO.roles.RolEditarDTO;
import com.istore.appweb.DTO.roles.RolEliminarDTO;
import com.istore.appweb.services.RolesServices;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/roles")
public class AdminRolesController {

  private final String FRAGMENTO = "tablaRoles";
  private final String VISTA_FRAGMENTO = "administrador/tablasBD/roles :: " + FRAGMENTO;

  @Autowired
  private RolesServices servicio;

  // Enpoint AJAX
  @GetMapping("/tabla")
  public String obtenerTodo(Model model) {
    prepararVista(model);

    return VISTA_FRAGMENTO;
  }

  @GetMapping
  public String listarTodo(Model model) {
    prepararVista(model);

    return VISTA_FRAGMENTO;
  }

  @PostMapping("/agregar")
  public String agregar(@Valid @ModelAttribute("rolAgregarDto") RolAgregarDTO rolAgregarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_FRAGMENTO;
    }

    try {
      servicio.createRol(rolAgregarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_FRAGMENTO;
    }

    model.addAttribute("rolAgregarDto", new RolAgregarDTO());
    prepararVista(model);

    return VISTA_FRAGMENTO;
  }

  @PostMapping("/editar")
  public String editar(@Valid @ModelAttribute("rolEditarDto") RolEditarDTO rolEditarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_FRAGMENTO;
    }

    try {
      servicio.updateRol(rolEditarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_FRAGMENTO;
    }

    prepararVista(model);

    return VISTA_FRAGMENTO;
  }

  @PostMapping("/eliminar")
  public String eliminar(@ModelAttribute RolEliminarDTO rolDTO, Model model) {
    servicio.deleteRol(rolDTO.getIdRol());
    prepararVista(model);

    return VISTA_FRAGMENTO;
  }

  private void prepararVista(Model model) {
    if (!model.containsAttribute("rolAgregarDto")) {
      model.addAttribute("rolAgregarDto", new RolAgregarDTO("", 0));
    }
    if (!model.containsAttribute("rolEditarDto")) {
      model.addAttribute("rolEditarDto", new RolEditarDTO());
    }

    model.addAttribute("roles", servicio.getRoles());
  }
}
