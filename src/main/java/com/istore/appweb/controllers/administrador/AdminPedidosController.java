package com.istore.appweb.controllers.administrador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.istore.appweb.DTO.pedidos.PedidoAgregarDTO;
import com.istore.appweb.DTO.pedidos.PedidoEditarDTO;
import com.istore.appweb.DTO.pedidos.PedidoEliminarDTO;
import com.istore.appweb.DTO.pedidosItems.PedidoItemAgregarDTO;
import com.istore.appweb.DTO.pedidosItems.PedidoItemEditarDTO;
import com.istore.appweb.entities.Pedidos;
import com.istore.appweb.entities.PedidosItems;
import com.istore.appweb.repositories.EstadosComprasRepository;
import com.istore.appweb.repositories.MetodosPagosRepository;
import com.istore.appweb.repositories.ProductosRepository;
import com.istore.appweb.repositories.TiposComprobantesRepository;
import com.istore.appweb.repositories.UsuariosRepository;
import com.istore.appweb.services.PedidosItemsServices;
import com.istore.appweb.services.PedidosServices;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/pedidos")
public class AdminPedidosController {

  private final String CARPETA_BASE = "administrador/tablasBD/";
  private final String VISTA_LISTAR = CARPETA_BASE + "pedidos";
  private final String REDIRECCIONAR = "redirect:/admin/pedidos";

  @Autowired
  private PedidosServices servicio;

  @Autowired
  private PedidosItemsServices servicioPedidosItems;

  @Autowired
  private UsuariosRepository repoUsuarios;

  @Autowired
  private MetodosPagosRepository repoMetodosPago;

  @Autowired
  private TiposComprobantesRepository repoTiposComprobante;

  @Autowired
  private EstadosComprasRepository repoEstadosCompras;

  @Autowired
  private ProductosRepository repoProductos;

  @GetMapping
  public String listarTodo(Model model) {
    prepararVista(model);

    return VISTA_LISTAR;
  }

  @PostMapping("/agregar")
  public String agregar(@Valid @ModelAttribute("pedidoAgregarDto") PedidoAgregarDTO pedidoAgregarDto,
      BindingResult result,
      Model model) {

    // Manejo de error de validación
    if (result.hasErrors()) {
      model.addAttribute("pedidoAgregarDto", pedidoAgregarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_LISTAR;
    }

    // Manejo de error de servicio
    try {
      servicio.create(pedidoAgregarDto);
    } catch (RuntimeException e) {
      // Captura "Usuario no encontrado", "MetodoPago no encontrado", etc.
      model.addAttribute("errorNullAgregar", e.getMessage());

      model.addAttribute("pedidoAgregarDto", pedidoAgregarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_LISTAR;
    }

    return REDIRECCIONAR;
  }

  @PostMapping("/editar")
  public String editar(@Valid @ModelAttribute("pedidoEditarDto") PedidoEditarDTO pedidoEditarDto,
      BindingResult result,
      Model model) {

    if (result.hasErrors()) {
      model.addAttribute("pedidoEditarDto", pedidoEditarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");
      return VISTA_LISTAR;
    }

    try {
      servicio.update(pedidoEditarDto);
    } catch (RuntimeException e) {
      // Captura "Usuario no encontrado", "MetodoPago no encontrado", etc.
      model.addAttribute("errorNullEditar", e.getMessage());

      model.addAttribute("pedidoEditarDto", pedidoEditarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_LISTAR;
    }

    return REDIRECCIONAR;
  }

  @PostMapping("/eliminar")
  public String eliminar(@ModelAttribute PedidoEliminarDTO pedidoEliminarDTO) {
    servicio.deleteById(pedidoEliminarDTO.getIdPedido());

    return REDIRECCIONAR;
  }

  private void prepararVista(Model model) {
    // Carga DTOs vacíos para los modales en caso no este ninguno activo
    if (!model.containsAttribute("pedidoAgregarDto")) {
      model.addAttribute("pedidoAgregarDto", new PedidoAgregarDTO());
    }
    if (!model.containsAttribute("pedidoEditarDto")) {
      model.addAttribute("pedidoEditarDto", new PedidoEditarDTO());
    }

    // Carga la lista principal
    model.addAttribute("pedidos", servicio.getAll());
    // Carga las entidades de los <select>
    model.addAttribute("usuarios", repoUsuarios.findAll());
    model.addAttribute("metodosPagos", repoMetodosPago.findAll());
    model.addAttribute("tiposComprobantes", repoTiposComprobante.findAll());
    model.addAttribute("estadosCompras", repoEstadosCompras.findAll());
  }

  /*
   * ###################################################
   * # NUEVA VISTA PARA VER Y GESTIONAR LOS ITEMS DEL PEDIDO #
   * ###################################################
   */

  @GetMapping("/detalles/{idPedido}")
  public String verDetallesPedido(@PathVariable("idPedido") Integer idPedido,
      Model model,
      RedirectAttributes redirectAttributes) {
    try {
      Pedidos pedido = servicio.getById(idPedido);
      model.addAttribute("pedido", pedido);

      // Cargar los Items (Detalle)
      List<PedidosItems> items = servicioPedidosItems.getAllByIdPedido(idPedido);
      model.addAttribute("pedidoItems", items);

    } catch (RuntimeException e) {
      // Si el pedido no existe, redirigimos a la lista principal
      redirectAttributes.addFlashAttribute("errorGeneral", "Error: " + e.getMessage());

      return REDIRECCIONAR;
    }

    if (!model.containsAttribute("pedidoItemAgregarDto")) {
      PedidoItemAgregarDTO pedidoItemAgregarDTO = new PedidoItemAgregarDTO();
      pedidoItemAgregarDTO.setCantidad(1);

      model.addAttribute("pedidoItemAgregarDto", pedidoItemAgregarDTO);
    }
    if (!model.containsAttribute("pedidoItemEditarDto")) {
      model.addAttribute("pedidoItemEditarDto", new PedidoItemEditarDTO());
    }

    // Cargar la lista de Productos para el <select> del modal "Agregar Item"
    model.addAttribute("productos", repoProductos.findAll());

    // Retornar la nueva vista HTML
    return "tablasBD/detalles-pedido";
  }

}
