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

  private final String CARPETA_BASE = "tablasBD/";
  private final String VISTA_LISTAR = CARPETA_BASE + "roles";
  private final String REDIRECCIONAR = "redirect:/admin/roles";

  @Autowired
  private RolesServices servicio;

  @GetMapping
  public String listarTodo(Model model) {
    prepararVista(model);

    return VISTA_LISTAR;
  }

  @PostMapping("/agregar")
  public String agregar(@Valid @ModelAttribute("rolAgregarDto") RolAgregarDTO rolAgregarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      model.addAttribute("rolAgregarDto", rolAgregarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_LISTAR;
    }

    try {
      servicio.createRol(rolAgregarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      model.addAttribute("rolAgregarDto", rolAgregarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_LISTAR;
    }

    return REDIRECCIONAR;
  }

  @PostMapping("/editar")
  public String editar(@Valid @ModelAttribute("rolEditarDto") RolEditarDTO rolEditarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      model.addAttribute("rolEditarDto", rolEditarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_LISTAR;
    }

    try {
      servicio.updateRol(rolEditarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      model.addAttribute("rolEditarDto", rolEditarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_LISTAR;
    }

    return REDIRECCIONAR;
  }

  @PostMapping("/eliminar")
  public String eliminar(@ModelAttribute RolEliminarDTO rolDTO) {
    servicio.deleteRol(rolDTO.getIdRol());

    return REDIRECCIONAR;
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
