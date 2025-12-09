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

  private final String FRAGMENTO = "tablaMetodosPagos";
  private final String VISTA_FRAGMENTO = "administrador/tablasBD/metodosPagos :: " + FRAGMENTO;

  @Autowired
  private MetodosPagosServices servicio;

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
  public String agregar(@Valid @ModelAttribute("metodoPagoAgregarDto") MetodoPagoAgregarDTO metodoPagoAgregarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_FRAGMENTO;
    }

    try {
      servicio.create(metodoPagoAgregarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_FRAGMENTO;
    }

    model.addAttribute("metodoPagoAgregarDto", new MetodoPagoAgregarDTO());
    prepararVista(model);

    return VISTA_FRAGMENTO;
  }

  @PostMapping("/editar")
  public String editar(@Valid @ModelAttribute("metodoPagoEditarDto") MetodoPagoEditarDTO metodoPagoEditarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_FRAGMENTO;
    }

    try {
      servicio.update(metodoPagoEditarDto);
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
  public String eliminar(@ModelAttribute MetodoPagoEliminarDTO metodoPagoEliminarDTO, Model model) {
    servicio.deleteById(metodoPagoEliminarDTO.getIdMetodoPago());
    prepararVista(model);

    return VISTA_FRAGMENTO;
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
