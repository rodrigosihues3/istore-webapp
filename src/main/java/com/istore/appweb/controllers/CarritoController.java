package com.istore.appweb.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.istore.appweb.services.CarritoService;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

  @Autowired
  private CarritoService carritoService;

  // API JSON: Agregar item (Usado por los botones del catálogo)
  @PostMapping("/agregar")
  @ResponseBody
  public ResponseEntity<Map<String, Object>> agregarAlCarrito(@RequestBody Map<String, Integer> payload) {
    Integer idProducto = payload.get("idProducto");
    Integer cantidad = payload.get("cantidad");

    carritoService.agregarItem(idProducto, cantidad);

    // Devolvemos la nueva cantidad total para actualizar el badge
    Map<String, Object> response = new HashMap<>();
    response.put("ok", true);
    response.put("cantidadTotal", carritoService.getCantidadItems());

    return ResponseEntity.ok(response);
  }

  // VISTA (Fragmento): Obtener el HTML del carrito actualizado (Para el Modal)
  @GetMapping("/vista")
  public String obtenerVistaCarrito(Model model) {
    model.addAttribute("items", carritoService.getItems());
    model.addAttribute("total", carritoService.getTotalGeneral());
    
    return "fragments/modals :: contenido-carrito";
  }

  @GetMapping("/info")
  @ResponseBody
  public ResponseEntity<Map<String, Object>> obtenerInfoCarrito() {
    Map<String, Object> response = new HashMap<>();
    response.put("cantidadTotal", carritoService.getCantidadItems());
    return ResponseEntity.ok(response);
  }

  @PostMapping("/actualizar")
  @ResponseBody
  public ResponseEntity<Map<String, Object>> actualizarCantidad(@RequestBody Map<String, Integer> payload) {
    Integer idProducto = payload.get("idProducto");
    Integer cantidad = payload.get("cantidad");

    carritoService.actualizarCantidad(idProducto, cantidad);

    Map<String, Object> response = new HashMap<>();
    response.put("ok", true);
    response.put("cantidadTotal", carritoService.getCantidadItems());

    return ResponseEntity.ok(response);
  }

  // API JSON: Eliminar item (Desde el modal)
  @PostMapping("/eliminar")
  @ResponseBody
  public ResponseEntity<Map<String, Object>> eliminarItem(@RequestBody Map<String, Integer> payload) {
    Integer idProducto = payload.get("idProducto");
    carritoService.removerItem(idProducto);

    Map<String, Object> response = new HashMap<>();
    response.put("ok", true);
    response.put("cantidadTotal", carritoService.getCantidadItems());
    response.put("totalGeneral", carritoService.getTotalGeneral());

    return ResponseEntity.ok(response);
  }
}
