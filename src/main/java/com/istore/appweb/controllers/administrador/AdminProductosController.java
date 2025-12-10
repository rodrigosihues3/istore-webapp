package com.istore.appweb.controllers.administrador;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.istore.appweb.DTO.productos.ProductoAgregarDTO;
import com.istore.appweb.DTO.productos.ProductoEditarDTO;
import com.istore.appweb.DTO.productos.ProductoEliminarDTO;
import com.istore.appweb.services.ProductosServices;
import com.istore.appweb.services.CategoriasServices;
import com.istore.appweb.services.ColoresServices;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/productos")
public class AdminProductosController {

  private final String FRAGMENTO = "tablaProductos";
  private final String VISTA_FRAGMENTO = "administrador/tablasBD/productos :: " + FRAGMENTO;

  @Autowired
  private ProductosServices servicio;

  @Autowired
  private CategoriasServices servicioCategorias;

  @Autowired
  private ColoresServices servicioColores;

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
  public String agregar(@Valid @ModelAttribute("productoAgregarDto") ProductoAgregarDTO productoAgregarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");
      return VISTA_FRAGMENTO;
    }

    try {
      servicio.createProducto(productoAgregarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      } else {
        // Fallback para mensajes sin prefijo
        model.addAttribute("mensajeError", e.getMessage());
      }

      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");
      return VISTA_FRAGMENTO;
    } catch (IOException e) {
      // Error al subir imagen
      model.addAttribute("mensajeError", "Error al subir la imagen: " + e.getMessage());
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_FRAGMENTO;
    }

    model.addAttribute("productoAgregarDto", new ProductoAgregarDTO());
    prepararVista(model);

    return VISTA_FRAGMENTO;
  }

  @PostMapping("/editar")
  public String editar(@Valid @ModelAttribute("productoEditarDto") ProductoEditarDTO productoEditarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_FRAGMENTO;
    }

    try {
      servicio.updateProducto(productoEditarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_FRAGMENTO;
    } catch (IOException e) {
      model.addAttribute("mensajeError", "Error al actualizar imagen: " + e.getMessage());
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_FRAGMENTO;
    }

    prepararVista(model);

    return VISTA_FRAGMENTO;
  }

  @PostMapping("/eliminar")
  public String eliminar(@ModelAttribute ProductoEliminarDTO productoEliminarDto,
      RedirectAttributes redirectAttributes, Model model) {
    try {
      servicio.deleteProducto(productoEliminarDto.getIdProducto());
      redirectAttributes.addFlashAttribute("mensajeExito", "Producto eliminado correctamente.");
    } catch (DataIntegrityViolationException e) {
      // Este error salta si el producto está en Pedidos, Carritos o Inventario
      redirectAttributes.addFlashAttribute("mensajeError",
          "No se puede eliminar el producto porque tiene registros asociados (Pedidos, Inventario o Carritos).");
    } catch (Exception e) {
      // Cualquier otro error imprevisto
      redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error inesperado al intentar eliminar.");
    }

    prepararVista(model);

    return VISTA_FRAGMENTO;
  }

  private void prepararVista(Model model) {
    if (!model.containsAttribute("productoAgregarDto")) {
      model.addAttribute("productoAgregarDto", new ProductoAgregarDTO());
    }
    if (!model.containsAttribute("productoEditarDto")) {
      model.addAttribute("productoEditarDto", new ProductoEditarDTO());
    }

    model.addAttribute("productos", servicio.getProductos());
    model.addAttribute("categorias", servicioCategorias.getCategorias());
    model.addAttribute("colores", servicioColores.getColores());
  }
}
