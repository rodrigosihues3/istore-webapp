package com.istore.appweb.controllers.administrador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.istore.appweb.DTO.tiposComprobantes.TipoComprobanteAgregarDTO;
import com.istore.appweb.DTO.tiposComprobantes.TipoComprobanteEditarDTO;
import com.istore.appweb.DTO.tiposComprobantes.TipoComprobanteEliminarDTO;
import com.istore.appweb.services.TiposComprobantesServices;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/tipos-comprobantes")
public class AdminTiposComprobantesController {

  private final String FRAGMENTO = "tablaTiposComprobantes";
  private final String VISTA_FRAGMENTO = "administrador/tablasBD/tiposComprobantes :: " + FRAGMENTO;

  @Autowired
  private TiposComprobantesServices servicio;

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
  public String agregar(
      @Valid @ModelAttribute("tipoComprobanteAgregarDto") TipoComprobanteAgregarDTO tipoComprobanteAgregarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_FRAGMENTO;
    }

    try {
      servicio.create(tipoComprobanteAgregarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_FRAGMENTO;
    }

    model.addAttribute("tipoComprobanteAgregarDto", new TipoComprobanteAgregarDTO());
    prepararVista(model);

    return VISTA_FRAGMENTO;
  }

  @PostMapping("/editar")
  public String editar(
      @Valid @ModelAttribute("tipoComprobanteEditarDto") TipoComprobanteEditarDTO tipoComprobanteEditarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_FRAGMENTO;
    }

    try {
      servicio.update(tipoComprobanteEditarDto);
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
  public String eliminar(@ModelAttribute TipoComprobanteEliminarDTO tipoComprobanteEliminarDTO, Model model) {
    servicio.deleteById(tipoComprobanteEliminarDTO.getIdTipoComprobante());
    prepararVista(model);

    return VISTA_FRAGMENTO;
  }

  private void prepararVista(Model model) {
    if (!model.containsAttribute("tipoComprobanteAgregarDto")) {
      model.addAttribute("tipoComprobanteAgregarDto", new TipoComprobanteAgregarDTO());
    }
    if (!model.containsAttribute("tipoComprobanteEditarDto")) {
      model.addAttribute("tipoComprobanteEditarDto", new TipoComprobanteEditarDTO());
    }

    model.addAttribute("tiposComprobantes", servicio.getAll());
  }

}
