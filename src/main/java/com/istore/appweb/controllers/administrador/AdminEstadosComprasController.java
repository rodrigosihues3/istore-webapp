package com.istore.appweb.controllers.administrador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.istore.appweb.DTO.estadosCompras.EstadoCompraAgregarDTO;
import com.istore.appweb.DTO.estadosCompras.EstadoCompraEditarDTO;
import com.istore.appweb.DTO.estadosCompras.EstadoCompraEliminarDTO;
import com.istore.appweb.services.EstadosComprasServices;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/estados-compras")
public class AdminEstadosComprasController {

  private final String FRAGMENTO = "tablaEstadosCompras";
  private final String VISTA_FRAGMENTO = "administrador/tablasBD/estadosCompras :: " + FRAGMENTO;

  @Autowired
  private EstadosComprasServices servicio;

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
  public String agregar(@Valid @ModelAttribute("estadoCompraAgregarDto") EstadoCompraAgregarDTO estadoCompraAgregarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_FRAGMENTO;
    }

    try {
      servicio.create(estadoCompraAgregarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_FRAGMENTO;
    }

    model.addAttribute("estadoCompraAgregarDto", new EstadoCompraAgregarDTO());
    prepararVista(model);

    return VISTA_FRAGMENTO;
  }

  @PostMapping("/editar")
  public String editar(@Valid @ModelAttribute("estadoCompraEditarDto") EstadoCompraEditarDTO estadoCompraEditarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_FRAGMENTO;
    }

    try {
      servicio.update(estadoCompraEditarDto);
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
  public String eliminar(@ModelAttribute EstadoCompraEliminarDTO estadoCompraEliminarDTO, Model model) {
    servicio.deleteById(estadoCompraEliminarDTO.getIdEstadoCompra());
    prepararVista(model);

    return VISTA_FRAGMENTO;
  }

  private void prepararVista(Model model) {
    if (!model.containsAttribute("estadoCompraAgregarDto")) {
      model.addAttribute("estadoCompraAgregarDto", new EstadoCompraAgregarDTO());
    }
    if (!model.containsAttribute("estadoCompraEditarDto")) {
      model.addAttribute("estadoCompraEditarDto", new EstadoCompraEditarDTO());
    }

    model.addAttribute("estadosCompras", servicio.getAll());
  }

}
