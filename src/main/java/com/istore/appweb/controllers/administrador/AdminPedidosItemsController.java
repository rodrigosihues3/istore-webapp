package com.istore.appweb.controllers.administrador;

import com.istore.appweb.DTO.pedidosItems.PedidoItemAgregarDTO;
import com.istore.appweb.DTO.pedidosItems.PedidoItemEditarDTO;
import com.istore.appweb.DTO.pedidosItems.PedidoItemEliminarDTO;
import com.istore.appweb.services.PedidosItemsServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/pedido-items") // Ruta base para las acciones
public class AdminPedidosItemsController {

  private final String VISTA_FRAGMENTO_DETALLE = "administrador/tablasBD/detalles-pedido :: contenidoDetalle";

  @Autowired
  private PedidosItemsServices servicio;

  @Autowired
  private AdminPedidosController pedidosController;

  @PostMapping("/agregar")
  public String agregar(@Valid @ModelAttribute("pedidoItemAgregarDto") PedidoItemAgregarDTO dto,
      BindingResult result,
      Model model) {
    Integer idPedido = dto.getIdPedido();

    if (result.hasErrors()) {
      pedidosController.prepararVistaDetalle(idPedido, model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_FRAGMENTO_DETALLE;
    }

    try {
      servicio.create(dto);
    } catch (RuntimeException e) {
      model.addAttribute("errorNullAgregarPedidoItem", e.getMessage());
      pedidosController.prepararVistaDetalle(idPedido, model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_FRAGMENTO_DETALLE;
    }

    model.addAttribute("pedidoItemAgregarDto", new PedidoItemAgregarDTO());
    pedidosController.prepararVistaDetalle(idPedido, model);

    return VISTA_FRAGMENTO_DETALLE;
  }

  @PostMapping("/editar")
  public String editar(@Valid @ModelAttribute("pedidoItemEditarDto") PedidoItemEditarDTO dto,
      BindingResult result,
      Model model) {
    Integer idPedido = null;

    try {
      idPedido = servicio.getById(dto.getIdPedidoItem()).getPedido().getIdPedido();
    } catch (Exception e) {
      return "redirect:/admin"; // Fallback de emergencia
    }

    // Manejo de error de validación
    if (result.hasErrors()) {
      pedidosController.prepararVistaDetalle(idPedido, model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_FRAGMENTO_DETALLE;
    }

    // Manejo de error de servicio
    try {
      servicio.update(dto);
    } catch (RuntimeException e) {
      model.addAttribute("errorNullEditarPedidoItem", e.getMessage());
      pedidosController.prepararVistaDetalle(idPedido, model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_FRAGMENTO_DETALLE;
    }

    model.addAttribute("pedidoItemEditarDto", new PedidoItemEditarDTO());
    pedidosController.prepararVistaDetalle(idPedido, model);

    return VISTA_FRAGMENTO_DETALLE;
  }

  @PostMapping("/eliminar")
  public String eliminar(@ModelAttribute PedidoItemEliminarDTO dto,
      Model model) {
    Integer idPedido = null;

    try {
      idPedido = servicio.getById(dto.getIdPedidoItem()).getPedido().getIdPedido();
      servicio.delete(dto.getIdPedidoItem());
    } catch (RuntimeException e) {
      model.addAttribute("errorGeneral", e.getMessage());

      return VISTA_FRAGMENTO_DETALLE;
    }

    pedidosController.prepararVistaDetalle(idPedido, model);

    return VISTA_FRAGMENTO_DETALLE;
  }
}