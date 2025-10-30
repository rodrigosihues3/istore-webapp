package com.istore.appweb.controllers.administrador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.istore.appweb.DTO.colores.ColorAgregarDTO;
import com.istore.appweb.DTO.colores.ColorEditarDTO;
import com.istore.appweb.DTO.colores.ColorEliminarDTO;
import com.istore.appweb.services.ColoresServices;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/colores")
public class AdminColoresController {

  private final String CARPETA_BASE = "tablasBD/";
  private final String VISTA_LISTAR = CARPETA_BASE + "colores";
  private final String REDIRECCIONAR = "redirect:/admin/colores";

  @Autowired
  private ColoresServices servicio;

  @GetMapping
  public String listarTodo(Model model) {
    prepararVista(model);

    return VISTA_LISTAR;
  }

  @PostMapping("/agregar")
  public String agregar(@Valid @ModelAttribute("colorAgregarDto") ColorAgregarDTO colorAgregarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      model.addAttribute("colorAgregarDto", colorAgregarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_LISTAR;
    }

    try {
      servicio.createColor(colorAgregarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      model.addAttribute("colorAgregarDto", colorAgregarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_LISTAR;
    }

    return REDIRECCIONAR;
  }

  @PostMapping("/editar")
  public String editar(@Valid @ModelAttribute("colorEditarDto") ColorEditarDTO colorEditarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      model.addAttribute("colorEditarDto", colorEditarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_LISTAR;
    }

    try {
      servicio.updateColor(colorEditarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      model.addAttribute("colorEditarDto", colorEditarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_LISTAR;
    }

    return REDIRECCIONAR;
  }

  @PostMapping("/eliminar")
  public String eliminar(@ModelAttribute ColorEliminarDTO colorDTO) {
    servicio.deleteById(colorDTO.getIdColor());

    return REDIRECCIONAR;
  }

  private void prepararVista(Model model) {
    if (!model.containsAttribute("colorAgregarDto")) {
      model.addAttribute("colorAgregarDto", new ColorAgregarDTO());
    }
    if (!model.containsAttribute("colorEditarDto")) {
      model.addAttribute("colorEditarDto", new ColorEditarDTO());
    }

    model.addAttribute("colores", servicio.getColores());
  }

}
