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

  private final String CARPETA_BASE = "tablasBD/";
  private final String VISTA_LISTAR = CARPETA_BASE + "estadosCompras";
  private final String REDIRECCIONAR = "redirect:/admin/estados-compras";

  @Autowired
  private EstadosComprasServices servicio;

  @GetMapping
  public String listarTodo(Model model) {
    prepararVista(model);

    return VISTA_LISTAR;
  }

  @PostMapping("/agregar")
  public String agregar(@Valid @ModelAttribute("estadoCompraAgregarDto") EstadoCompraAgregarDTO estadoCompraAgregarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      model.addAttribute("estadoCompraAgregarDto", estadoCompraAgregarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_LISTAR;
    }

    try {
      servicio.create(estadoCompraAgregarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      model.addAttribute("estadoCompraAgregarDto", estadoCompraAgregarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_LISTAR;
    }

    return REDIRECCIONAR;
  }

  @PostMapping("/editar")
  public String editar(@Valid @ModelAttribute("estadoCompraEditarDto") EstadoCompraEditarDTO estadoCompraEditarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      model.addAttribute("estadoCompraEditarDto", estadoCompraEditarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_LISTAR;
    }

    try {
      servicio.update(estadoCompraEditarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      model.addAttribute("estadoCompraEditarDto", estadoCompraEditarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_LISTAR;
    }

    return REDIRECCIONAR;
  }

  @PostMapping("/eliminar")
  public String eliminar(@ModelAttribute EstadoCompraEliminarDTO estadoCompraEliminarDTO) {
    servicio.deleteById(estadoCompraEliminarDTO.getIdEstadoCompra());

    return REDIRECCIONAR;
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
