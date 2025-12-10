package com.istore.appweb.controllers.clientes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.istore.appweb.entities.Pedidos;
import com.istore.appweb.entities.Usuarios;
import com.istore.appweb.services.PedidosItemsServices;
import com.istore.appweb.services.PedidosServices;
import com.istore.appweb.services.UsuariosServices;

@Controller
@RequestMapping("/mi-cuenta/pedidos")
public class ClientePedidosController {

  @Autowired
  private PedidosServices pedidosService;

  @Autowired
  private PedidosItemsServices itemsService;

  @Autowired
  private UsuariosServices usuariosService;

  // 1. LISTA DE PEDIDOS DEL CLIENTE
  @GetMapping
  public String listarMisPedidos(Model model, Authentication auth) {
    // Obtenemos el usuario logueado
    Usuarios usuarioLogueado = getUsuarioActual(auth);

    // Buscamos SOLO sus pedidos
    model.addAttribute("pedidos", pedidosService.getPedidosByUsuario(usuarioLogueado));

    return "clientes/pedidos"; // Vista que crearemos en el siguiente paso
  }

  // 2. DETALLE DEL PEDIDO (AJAX/MODAL)
  @GetMapping("/detalle/{id}")
  public String verDetallePedido(@PathVariable Integer id, Model model, Authentication auth) {
    Usuarios usuarioLogueado = getUsuarioActual(auth);

    try {
      Pedidos pedido = pedidosService.getById(id);

      // SEGURIDAD: Verificar que el pedido pertenezca al usuario logueado
      if (!pedido.getUsuario().getIdUsuario().equals(usuarioLogueado.getIdUsuario())) {
        return "error/403"; // Acceso denegado si intenta ver el pedido de otro
      }

      model.addAttribute("pedido", pedido);
      model.addAttribute("items", itemsService.getAllByIdPedido(id));

      // Retornamos solo el fragmento para el modal
      return "clientes/modalDetallePedido :: contenidoModal";

    } catch (Exception e) {
      return "error/404";
    }
  }

  // Helper para obtener el objeto Usuario desde Spring Security
  private Usuarios getUsuarioActual(Authentication auth) {
    String username = auth.getName();
    return usuariosService.getUsuarioByNombreUsuario(username)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado en sesión"));
  }
}
