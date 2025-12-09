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

  private final String FRAGMENTO = "tablaColores";
  private final String VISTA_FRAGMENTO = "administrador/tablasBD/colores :: " + FRAGMENTO;

  @Autowired
  private ColoresServices servicio;

  // Enpoint AJAX
  @GetMapping("/tabla")
  public String obtenerTodo(Model model) {
    prepararVista(model);

    return VISTA_FRAGMENTO;
  }

  @GetMapping
  public String listarTodo(Model model) {
    prepararVista(model);

    return "redirect:/admin";
  }

  @PostMapping("/agregar")
  public String agregar(@Valid @ModelAttribute("colorAgregarDto") ColorAgregarDTO colorAgregarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_FRAGMENTO;
    }

    try {
      servicio.createColor(colorAgregarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_FRAGMENTO;
    }

    model.addAttribute("colorAgregarDto", new ColorAgregarDTO());
    prepararVista(model);

    return VISTA_FRAGMENTO;
  }

  @PostMapping("/editar")
  public String editar(@Valid @ModelAttribute("colorEditarDto") ColorEditarDTO colorEditarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_FRAGMENTO;
    }

    try {
      servicio.updateColor(colorEditarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2)
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);

      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_FRAGMENTO;
    }

    prepararVista(model);

    return VISTA_FRAGMENTO;
  }

  @PostMapping("/eliminar")
  public String eliminar(@ModelAttribute ColorEliminarDTO colorDTO, Model model) {
    servicio.deleteById(colorDTO.getIdColor());
    prepararVista(model);

    return VISTA_FRAGMENTO;
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
