package com.istore.appweb.controllers.administrador;

import com.istore.appweb.DTO.pedidosItems.PedidoItemAgregarDTO;
import com.istore.appweb.DTO.pedidosItems.PedidoItemEditarDTO;
import com.istore.appweb.DTO.pedidosItems.PedidoItemEliminarDTO;
import com.istore.appweb.entities.PedidosItems;
import com.istore.appweb.services.PedidosItemsServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/pedido-items") // Ruta base para las acciones
public class AdminPedidosItemsController {

  @Autowired
  private PedidosItemsServices servicio;

  private final String VISTA_DETALLES_REDIRECT = "redirect:/admin/pedidos/detalles/";

  @PostMapping("/agregar")
  public String agregar(@Valid @ModelAttribute("pedidoItemAgregarDto") PedidoItemAgregarDTO dto,
      BindingResult result,
      RedirectAttributes redirectAttributes) {
    String redirectUrl = VISTA_DETALLES_REDIRECT + dto.getIdPedido();

    // Manejo de error de validación
    if (result.hasErrors()) {
      // Flash Attributes para enviar los errores a la redirección
      redirectAttributes.addFlashAttribute("pedidoItemAgregarDto", dto);
      redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.pedidoItemAgregarDto", result);
      redirectAttributes.addFlashAttribute("mostrarModal", "#modalAgregar");

      return redirectUrl;
    }

    // Manejo de error de servicio
    try {
      servicio.create(dto);
    } catch (RuntimeException e) {
      redirectAttributes.addFlashAttribute("pedidoItemAgregarDto", dto);
      redirectAttributes.addFlashAttribute("errorNullAgregarPedidoItem", e.getMessage());
      redirectAttributes.addFlashAttribute("mostrarModal", "#modalAgregar");

      return redirectUrl;
    }

    redirectAttributes.addFlashAttribute("success", "Item agregado correctamente al pedido");
    return redirectUrl;
  }

  @PostMapping("/editar")
  public String editar(@Valid @ModelAttribute("pedidoItemEditarDto") PedidoItemEditarDTO dto,
      BindingResult result,
      RedirectAttributes redirectAttributes) {
    Integer idPedido;

    try {
      // Buscar item para saber el ID de su pedido
      PedidosItems item = servicio.getById(dto.getIdPedidoItem());
      idPedido = item.getPedido().getIdPedido();
    } catch (RuntimeException e) {
      redirectAttributes.addFlashAttribute("errorGeneral", e.getMessage());

      return "redirect:/admin/pedidos";
    }

    String redirectUrl = VISTA_DETALLES_REDIRECT + idPedido;

    // Manejo de error de validación
    if (result.hasErrors()) {
      redirectAttributes.addFlashAttribute("pedidoItemEditarDto", dto);
      redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.pedidoItemEditarDto", result);
      redirectAttributes.addFlashAttribute("mostrarModal", "#modalEditar");

      return redirectUrl;
    }

    // Manejo de error de servicio
    try {
      servicio.update(dto);
    } catch (RuntimeException e) {
      redirectAttributes.addFlashAttribute("pedidoItemEditarDto", dto);
      redirectAttributes.addFlashAttribute("errorNullEditarPedidoItem", e.getMessage());
      redirectAttributes.addFlashAttribute("mostrarModal", "#modalEditar");

      return redirectUrl;
    }

    redirectAttributes.addFlashAttribute("success", "Item editado correctamente del pedido");
    return redirectUrl;
  }

  @PostMapping("/eliminar")
  public String eliminar(@ModelAttribute PedidoItemEliminarDTO dto,
      RedirectAttributes redirectAttributes) {

    Integer idPedido;
    try {
      PedidosItems item = servicio.getById(dto.getIdPedidoItem());
      idPedido = item.getPedido().getIdPedido();

      servicio.delete(dto.getIdPedidoItem());

    } catch (RuntimeException e) {
      redirectAttributes.addFlashAttribute("errorGeneral", e.getMessage());

      return "redirect:/admin/pedidos";
    }

    redirectAttributes.addFlashAttribute("success", "Item eliminado correctamente del pedido");
    return VISTA_DETALLES_REDIRECT + idPedido;
  }
}