package com.istore.appweb.controllers.administrador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.istore.appweb.DTO.metodosPagos.MetodoPagoAgregarDTO;
import com.istore.appweb.DTO.metodosPagos.MetodoPagoEditarDTO;
import com.istore.appweb.DTO.metodosPagos.MetodoPagoEliminarDTO;
import com.istore.appweb.services.MetodosPagosServices;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/metodos-pagos")
public class AdminMetodosPagosController {

  private final String CARPETA_BASE = "tablasBD/";
  private final String VISTA_LISTAR = CARPETA_BASE + "metodosPagos";
  private final String REDIRECCIONAR = "redirect:/admin/metodos-pagos";

  @Autowired
  private MetodosPagosServices servicio;

  @GetMapping
  public String listarTodo(Model model) {
    prepararVista(model);

    return VISTA_LISTAR;
  }

  @PostMapping("/agregar")
  public String agregar(@Valid @ModelAttribute("metodoPagoAgregarDto") MetodoPagoAgregarDTO metodoPagoAgregarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      model.addAttribute("metodoPagoAgregarDto", metodoPagoAgregarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_LISTAR;
    }

    try {
      servicio.create(metodoPagoAgregarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      model.addAttribute("metodoPagoAgregarDto", metodoPagoAgregarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_LISTAR;
    }

    return REDIRECCIONAR;
  }

  @PostMapping("/editar")
  public String editar(@Valid @ModelAttribute("metodoPagoEditarDto") MetodoPagoEditarDTO metodoPagoEditarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      model.addAttribute("metodoPagoEditarDto", metodoPagoEditarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_LISTAR;
    }

    try {
      servicio.update(metodoPagoEditarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      model.addAttribute("metodoPagoEditarDto", metodoPagoEditarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_LISTAR;
    }

    return REDIRECCIONAR;
  }

  @PostMapping("/eliminar")
  public String eliminar(@ModelAttribute MetodoPagoEliminarDTO metodoPagoEliminarDTO) {
    servicio.deleteById(metodoPagoEliminarDTO.getIdMetodoPago());

    return REDIRECCIONAR;
  }

  private void prepararVista(Model model) {
    if (!model.containsAttribute("metodoPagoAgregarDto")) {
      model.addAttribute("metodoPagoAgregarDto", new MetodoPagoAgregarDTO());
    }
    if (!model.containsAttribute("metodoPagoEditarDto")) {
      model.addAttribute("metodoPagoEditarDto", new MetodoPagoEditarDTO());
    }

    model.addAttribute("metodosPagos", servicio.getAll());
  }

}
