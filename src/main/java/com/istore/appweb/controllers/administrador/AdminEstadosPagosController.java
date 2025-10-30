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

  private final String CARPETA_BASE = "tablasBD/";
  private final String VISTA_LISTAR = CARPETA_BASE + "estadosPagos";
  private final String REDIRECCIONAR = "redirect:/admin/estados-pagos";

  @Autowired
  private EstadosPagosServices servicio;

  @GetMapping
  public String listarTodo(Model model) {
    prepararVista(model);

    return VISTA_LISTAR;
  }

  @PostMapping("/agregar")
  public String agregar(@Valid @ModelAttribute("estadoPagoAgregarDto") EstadoPagoAgregarDTO estadoPagoAgregarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      model.addAttribute("estadoPagoAgregarDto", estadoPagoAgregarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_LISTAR;
    }

    try {
      servicio.create(estadoPagoAgregarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      model.addAttribute("estadoPagoAgregarDto", estadoPagoAgregarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_LISTAR;
    }

    return REDIRECCIONAR;
  }

  @PostMapping("/editar")
  public String editar(@Valid @ModelAttribute("estadoPagoEditarDto") EstadoPagoEditarDTO estadoPagoEditarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      model.addAttribute("estadoPagoEditarDto", estadoPagoEditarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_LISTAR;
    }

    try {
      servicio.update(estadoPagoEditarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      model.addAttribute("estadoPagoEditarDto", estadoPagoEditarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_LISTAR;
    }

    return REDIRECCIONAR;
  }

  @PostMapping("/eliminar")
  public String eliminar(@ModelAttribute EstadoPagoEliminarDTO estadoPagoEliminarDTO) {
    servicio.deleteById(estadoPagoEliminarDTO.getIdEstadoPago());

    return REDIRECCIONAR;
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
