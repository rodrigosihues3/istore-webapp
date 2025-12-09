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

  private final String VISTA_FRAGMENTO_LISTA = "administrador/tablasBD/pedidos :: tablaPedidos";
  private final String VISTA_FRAGMENTO_DETALLE = "administrador/tablasBD/detalles-pedido :: contenidoDetalle";

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

  // Endoint AJAX
  @GetMapping("/tabla")
  public String obtenerTodo(Model model) {
    prepararVista(model);

    return VISTA_FRAGMENTO_LISTA;
  }

  // 2. DETALLE DEL PEDIDO (AJAX)
  @GetMapping("/detalles/{idPedido}")
  public String verDetallesPedido(@PathVariable("idPedido") Integer idPedido, Model model) {
    prepararVistaDetalle(idPedido, model);

    return VISTA_FRAGMENTO_DETALLE;
  }

  @GetMapping
  public String listarTodo(Model model) {
    prepararVista(model);

    return "redirect:/admin";
  }

  @PostMapping("/agregar")
  public String agregar(@Valid @ModelAttribute("pedidoAgregarDto") PedidoAgregarDTO pedidoAgregarDto,
      BindingResult result,
      Model model) {

    if (result.hasErrors()) {
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_FRAGMENTO_LISTA;
    }

    try {
      servicio.create(pedidoAgregarDto);
    } catch (RuntimeException e) {
      model.addAttribute("errorNullAgregar", e.getMessage());
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_FRAGMENTO_LISTA;
    }

    model.addAttribute("pedidoAgregarDto", new PedidoAgregarDTO());
    prepararVista(model);

    return VISTA_FRAGMENTO_LISTA;
  }

  @PostMapping("/editar")
  public String editar(@Valid @ModelAttribute("pedidoEditarDto") PedidoEditarDTO pedidoEditarDto,
      BindingResult result,
      Model model) {

    if (result.hasErrors()) {
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_FRAGMENTO_LISTA;
    }

    try {
      servicio.update(pedidoEditarDto);
    } catch (RuntimeException e) {
      model.addAttribute("errorNullEditar", e.getMessage());
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_FRAGMENTO_LISTA;
    }

    model.addAttribute("pedidoEditarDto", new PedidoEditarDTO());
    prepararVista(model);

    return VISTA_FRAGMENTO_LISTA;
  }

  @PostMapping("/eliminar")
  public String eliminar(@ModelAttribute PedidoEliminarDTO pedidoEliminarDTO, Model model) {
    servicio.deleteById(pedidoEliminarDTO.getIdPedido());
    prepararVista(model);

    return VISTA_FRAGMENTO_LISTA;
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

  // Este método público será usado también por el controlador de Items
  public void prepararVistaDetalle(Integer idPedido, Model model) {
    try {
      Pedidos pedido = servicio.getById(idPedido);
      model.addAttribute("pedido", pedido);
      model.addAttribute("pedidoItems", servicioPedidosItems.getAllByIdPedido(idPedido));
    } catch (RuntimeException e) {
      model.addAttribute("errorGeneral", "Error al cargar pedido: " + e.getMessage());
    }

    if (!model.containsAttribute("pedidoItemAgregarDto")) {
      PedidoItemAgregarDTO dto = new PedidoItemAgregarDTO();
      dto.setCantidad(1);
      // Importante: Pre-llenar el ID del pedido para el formulario
      dto.setIdPedido(idPedido);
      model.addAttribute("pedidoItemAgregarDto", dto);
    }
    if (!model.containsAttribute("pedidoItemEditarDto")) {
      model.addAttribute("pedidoItemEditarDto", new PedidoItemEditarDTO());
    }

    model.addAttribute("productos", repoProductos.findAll());
  }
}
