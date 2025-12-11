package com.istore.appweb.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.istore.appweb.entities.*;
import com.istore.appweb.models.CarritoItem;
import com.istore.appweb.models.CheckoutForm;
import com.istore.appweb.repositories.*;
import com.istore.appweb.services.CarritoService;

@Controller
@RequestMapping("/cliente/checkout")
public class CheckoutController {

  @Autowired
  private CarritoService carritoService;
  @Autowired
  private UsuariosRepository repoUsuarios;
  @Autowired
  private PedidosRepository repoPedidos;
  @Autowired
  private ProductosRepository repoProductos;
  @Autowired
  private PedidosItemsRepository repoItems;

  // Repositorios auxiliares (Asumiendo que existen)
  @Autowired
  private EstadosComprasRepository repoEstadosCompra;
  @Autowired
  private EstadosPagosRepository repoEstadosPago;
  @Autowired
  private MetodosPagosRepository repoMetodosPago;
  @Autowired
  private TiposComprobantesRepository repoTiposComprobante;

  @GetMapping
  public String vistaCheckout(Model model, Authentication auth) {
    // 1. VALIDACIÓN DE SEGURIDAD
    if (auth == null || !auth.isAuthenticated()) {
      return "redirect:/iniciar-sesion";
    }

    // 2. VALIDACIÓN DE CARRITO VACÍO
    if (carritoService.getCantidadItems() == 0) {
      return "redirect:/catalogo";
    }

    // Datos del usuario para pre-llenar (Opcional)
    String nombreUsuario = auth.getName();
    Usuarios usuario = repoUsuarios.findByEmail(nombreUsuario)
        .or(() -> repoUsuarios.findByNombreUsuario(nombreUsuario))
        .orElse(new Usuarios());

    model.addAttribute("usuario", usuario);
    model.addAttribute("items", carritoService.getItems());
    model.addAttribute("total", carritoService.getTotalGeneral());

    return "clientes/checkout";
  }

  @PostMapping("/procesar")
  @ResponseBody
  public ResponseEntity<?> procesarPedido(@RequestBody CheckoutForm form, Authentication auth) {
    Map<String, Object> response = new HashMap<>();

    try {
      // VALIDACIÓN DE SEGURIDAD
      if (auth == null)
        throw new RuntimeException("Sesión expirada. Recarga la página.");

      // Validar Usuario
      String nombreUsuario = auth.getName();
      Usuarios usuario = repoUsuarios.findByEmail(nombreUsuario)
          .or(() -> repoUsuarios.findByNombreUsuario(nombreUsuario))
          .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

      // Validar Carrito y Stock
      List<CarritoItem> itemsCarrito = carritoService.getItems();
      if (itemsCarrito.isEmpty())
        throw new RuntimeException("El carrito está vacío");

      for (CarritoItem item : itemsCarrito) {
        if (item.getProducto().getStock() < item.getCantidad()) {
          throw new RuntimeException("Stock insuficiente para: " + item.getProducto().getNombre());
        }
      }

      // Crear Pedido (Cabecera)
      Pedidos pedido = new Pedidos();

      pedido.setUsuario(usuario);
      pedido.setTotal(carritoService.getTotalGeneral());
      pedido.setDireccionEntrega(form.getDireccionEntrega());
      pedido.setNumeroDocumento(form.getNumeroDocumento());
      pedido.setNombreEntidad(form.getNombreEntidad());
      pedido.setReferenciaPago(form.getReferenciaPago());
      pedido.setMetodoPago(repoMetodosPago.findByNombre(form.getMetodoPago())
          .orElseThrow(() -> new RuntimeException("Método de pago no válido")));
      pedido.setEstadoCompra(repoEstadosCompra.findByNombre("PENDIENTE").orElse(null));
      pedido.setEstadoPago(repoEstadosPago.findByNombre("PAGADO").orElse(null));
      pedido.setTipoComprobante(repoTiposComprobante.findByNombre(form.getTipoComprobante()).orElse(null));

      Pedidos pedidoGuardado = repoPedidos.save(pedido);

      // Guardar Items y Descontar Stock
      for (CarritoItem itemC : itemsCarrito) {
        PedidosItems itemBD = new PedidosItems();

        itemBD.setPedido(pedidoGuardado);
        itemBD.setProducto(itemC.getProducto());
        itemBD.setCantidad(itemC.getCantidad());
        itemBD.setPrecio(itemC.getProducto().getPrecio());
        itemBD.setTotal(itemC.getTotal());
        repoItems.save(itemBD);

        // Descontar Stock
        Productos prod = itemC.getProducto();

        prod.setStock(prod.getStock() - itemC.getCantidad());
        repoProductos.save(prod);
      }

      // Limpiar Carrito
      carritoService.limpiarCarrito();

      response.put("ok", true);
      response.put("idPedido", pedidoGuardado.getIdPedido());
      return ResponseEntity.ok(response);

    } catch (Exception e) {
      response.put("ok", false);
      response.put("mensaje", e.getMessage());
      return ResponseEntity.badRequest().body(response);
    }
  }
}
