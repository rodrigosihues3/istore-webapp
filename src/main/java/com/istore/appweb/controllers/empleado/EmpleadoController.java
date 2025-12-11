package com.istore.appweb.controllers.empleado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.istore.appweb.entities.EstadosCompras;
import com.istore.appweb.entities.Pedidos;
import com.istore.appweb.entities.PedidosItems;
import com.istore.appweb.entities.Productos;
import com.istore.appweb.repositories.EstadosComprasRepository;
import com.istore.appweb.repositories.PedidosRepository;
import com.istore.appweb.repositories.ProductosRepository;
import com.istore.appweb.services.PedidosItemsServices;
import com.istore.appweb.services.ProductosServices;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/empleado")
public class EmpleadoController {

  private final String VISTA_TABLA_PEDIDOS = "empleado/pedidos :: tablaPedidos";
  private final String VISTA_TABLA_INVENTARIO = "empleado/inventario :: tablaInventario";

  @Autowired
  private PedidosRepository repoPedidos;

  @Autowired
  private EstadosComprasRepository repoEstados;

  @Autowired
  private ProductosRepository repoProductos;

  @Autowired
  private PedidosItemsServices itemsService;

  @Autowired
  private ProductosServices productosService;

  // 1. DASHBOARD PRINCIPAL (Estadísticas + Sidebar)
  @GetMapping()
  public String dashboard(Model model) {
    List<Pedidos> todos = repoPedidos.findAll();

    model.addAttribute("cantPendientes",
        todos.stream().filter(p -> p.getEstadoCompra().getNombre().equals("PENDIENTE")).count());
    model.addAttribute("cantConfirmados",
        todos.stream().filter(p -> p.getEstadoCompra().getNombre().equals("CONFIRMADO")).count());
    model.addAttribute("cantEntregados",
        todos.stream().filter(p -> p.getEstadoCompra().getNombre().equals("ENTREGADO")).count());

    return "empleado/index-empleado";
  }

  // 2. VISTA DE PEDIDOS (Carga Completa)
  @GetMapping("/pedidos")
  public String pedidos(Model model) {
    cargarTablaPedidos(model);
    return "empleado/pedidos";
  }

  // 3. ENDPOINT AJAX: Tabla Pedidos (Fragmento)
  @GetMapping("/pedidos/tabla")
  public String obtenerTablaPedidos(Model model) {
    cargarTablaPedidos(model);
    return VISTA_TABLA_PEDIDOS;
  }

  // 4. ENDPOINT AJAX: Tabla Inventario (Fragmento)
  @GetMapping("/inventario/tabla")
  public String obtenerTablaInventario(Model model) {
    model.addAttribute("productos", productosService.getProductos());
    return VISTA_TABLA_INVENTARIO;
  }

  // 5. ACCIÓN: CAMBIAR ESTADO
  @PostMapping("/pedidos/cambiar-estado")
  public String cambiarEstado(@RequestParam Integer idPedido,
      @RequestParam String accion,
      Model model) {
    try {
      Pedidos pedido = repoPedidos.findById(idPedido)
          .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

      String estadoActual = pedido.getEstadoCompra().getNombre();
      String nuevoEstadoNombre = "";
      boolean actualizarStock = false;
      boolean esDevolucion = false;

      switch (accion) {
        case "CONFIRMAR":
          if (!estadoActual.equals("PENDIENTE"))
            throw new RuntimeException("Solo se pueden confirmar pedidos pendientes.");
          nuevoEstadoNombre = "CONFIRMADO";
          break;
        case "ENTREGAR":
          if (!estadoActual.equals("CONFIRMADO"))
            throw new RuntimeException("Debe estar confirmado antes de entregar.");
          nuevoEstadoNombre = "ENTREGADO";
          break;
        case "CANCELAR":
          nuevoEstadoNombre = "CANCELADO";
          if (estadoActual.equals("CONFIRMADO")) {
            actualizarStock = true;
            esDevolucion = true;
          }
          break;
      }

      if (actualizarStock) {
        List<PedidosItems> items = itemsService.getAllByIdPedido(idPedido);
        for (PedidosItems item : items) {
          Productos producto = item.getProducto();
          int nuevoStock = esDevolucion ? producto.getStock() + item.getCantidad()
              : producto.getStock() - item.getCantidad();

          if (nuevoStock < 0)
            throw new RuntimeException("Stock insuficiente.");
          producto.setStock(nuevoStock);
          repoProductos.save(producto);
        }
      }

      EstadosCompras nuevoEstado = repoEstados.findByNombre(nuevoEstadoNombre).orElseThrow();
      pedido.setEstadoCompra(nuevoEstado);
      pedido.setFechaActualizacion(LocalDateTime.now());
      repoPedidos.save(pedido);

      model.addAttribute("mensajeExito", "Pedido #" + idPedido + " -> " + nuevoEstadoNombre);

    } catch (Exception e) {
      model.addAttribute("mensajeError", "Error: " + e.getMessage());
    }

    cargarTablaPedidos(model);
    return VISTA_TABLA_PEDIDOS;
  }

  // 6. DETALLE RÁPIDO (Modal)
  @GetMapping("/pedidos/detalle/{id}")
  public String verDetalle(@PathVariable Integer id, Model model) {
    Pedidos pedido = repoPedidos.findById(id).orElse(null);
    if (pedido == null)
      return "fragments/error :: 404";
    model.addAttribute("pedido", pedido);
    model.addAttribute("items", itemsService.getAllByIdPedido(id));
    return "clientes/modalDetallePedido :: contenidoModal";
  }

  // 7. INVENTARIO (Vista Completa)
  @GetMapping("/inventario") // CORREGIDO: Faltaba esta anotación
  public String inventario(Model model) {
    model.addAttribute("productos", productosService.getProductos());
    return "empleado/inventario";
  }

  private void cargarTablaPedidos(Model model) {
    List<Pedidos> cola = repoPedidos.findAll().stream()
        .filter(p -> {
          String e = p.getEstadoCompra().getNombre();
          // AHORA INCLUIMOS 'ENTREGADO' PARA HISTORIAL
          return e.equals("PENDIENTE") || e.equals("CONFIRMADO") || e.equals("ENTREGADO");
        })
        // ORDENAMIENTO INTELIGENTE:
        // 1. PENDIENTE y CONFIRMADO van primero (Prioridad Alta)
        // 2. ENTREGADO va al final (Historial)
        // 3. Dentro de cada grupo, por fecha antigua a reciente (FIFO)
        .sorted((p1, p2) -> {
          int p1Score = getScoreEstado(p1.getEstadoCompra().getNombre());
          int p2Score = getScoreEstado(p2.getEstadoCompra().getNombre());

          if (p1Score != p2Score) {
            return Integer.compare(p1Score, p2Score);
          }
          return p1.getFechaCreacion().compareTo(p2.getFechaCreacion());
        })
        .collect(Collectors.toList());

    model.addAttribute("pedidos", cola);
  }

  // Helper para ordenar
  private int getScoreEstado(String estado) {
    switch (estado) {
      case "PENDIENTE":
        return 1;
      case "CONFIRMADO":
        return 2;
      default:
        return 3; // ENTREGADO, CANCELADO
    }
  }
}