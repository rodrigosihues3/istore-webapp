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

  private final String CARPETA_BASE = "tablasBD/";
  private final String VISTA_LISTAR = CARPETA_BASE + "tiposComprobantes";
  private final String REDIRECCIONAR = "redirect:/admin/tipos-comprobantes";

  @Autowired
  private TiposComprobantesServices servicio;

  @GetMapping
  public String listarTodo(Model model) {
    prepararVista(model);

    return VISTA_LISTAR;
  }

  @PostMapping("/agregar")
  public String agregar(
      @Valid @ModelAttribute("tipoComprobanteAgregarDto") TipoComprobanteAgregarDTO tipoComprobanteAgregarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      model.addAttribute("tipoComprobanteAgregarDto", tipoComprobanteAgregarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_LISTAR;
    }

    try {
      servicio.create(tipoComprobanteAgregarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      model.addAttribute("tipoComprobanteAgregarDto", tipoComprobanteAgregarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_LISTAR;
    }

    return REDIRECCIONAR;
  }

  @PostMapping("/editar")
  public String editar(
      @Valid @ModelAttribute("tipoComprobanteEditarDto") TipoComprobanteEditarDTO tipoComprobanteEditarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      model.addAttribute("tipoComprobanteEditarDto", tipoComprobanteEditarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_LISTAR;
    }

    try {
      servicio.update(tipoComprobanteEditarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      model.addAttribute("tipoComprobanteEditarDto", tipoComprobanteEditarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_LISTAR;
    }

    return REDIRECCIONAR;
  }

  @PostMapping("/eliminar")
  public String eliminar(@ModelAttribute TipoComprobanteEliminarDTO tipoComprobanteEliminarDTO) {
    servicio.deleteById(tipoComprobanteEliminarDTO.getIdTipoComprobante());

    return REDIRECCIONAR;
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
