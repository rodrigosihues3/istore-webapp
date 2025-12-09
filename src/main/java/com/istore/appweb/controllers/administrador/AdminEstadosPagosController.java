package com.istore.appweb.controllers.administrador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.istore.appweb.DTO.estadosPagos.EstadoPagoAgregarDTO;
import com.istore.appweb.DTO.estadosPagos.EstadoPagoEditarDTO;
import com.istore.appweb.DTO.estadosPagos.EstadoPagoEliminarDTO;
import com.istore.appweb.services.EstadosPagosServices;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/estados-pagos")
public class AdminEstadosPagosController {

  private final String FRAGMENTO = "tablaEstadosPagos";
  private final String VISTA_FRAGMENTO = "administrador/tablasBD/estadosPagos :: " + FRAGMENTO;

  @Autowired
  private EstadosPagosServices servicio;

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
  public String agregar(@Valid @ModelAttribute("estadoPagoAgregarDto") EstadoPagoAgregarDTO estadoPagoAgregarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_FRAGMENTO;
    }

    try {
      servicio.create(estadoPagoAgregarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_FRAGMENTO;
    }

    model.addAttribute("estadoPagoAgregarDto", new EstadoPagoAgregarDTO());
    prepararVista(model);

    return VISTA_FRAGMENTO;
  }

  @PostMapping("/editar")
  public String editar(@Valid @ModelAttribute("estadoPagoEditarDto") EstadoPagoEditarDTO estadoPagoEditarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_FRAGMENTO;
    }

    try {
      servicio.update(estadoPagoEditarDto);
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
  public String eliminar(@ModelAttribute EstadoPagoEliminarDTO estadoPagoEliminarDTO, Model model) {
    servicio.deleteById(estadoPagoEliminarDTO.getIdEstadoPago());
    prepararVista(model);

    return VISTA_FRAGMENTO;
  }

  private void prepararVista(Model model) {
    if (!model.containsAttribute("estadoPagoAgregarDto")) {
      model.addAttribute("estadoPagoAgregarDto", new EstadoPagoAgregarDTO());
    }
    if (!model.containsAttribute("estadoPagoEditarDto")) {
      model.addAttribute("estadoPagoEditarDto", new EstadoPagoEditarDTO());
    }

    model.addAttribute("estadosPagos", servicio.getAll());
  }

}
