package com.istore.appweb.controllers.administrador;

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

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/productos")
public class AdminProductosController {

  private final String CARPETA_BASE = "tablasBD/";
  private final String VISTA_LISTAR = CARPETA_BASE + "productos";
  private final String REDIRECCIONAR = "redirect:/admin/productos";

  @Autowired
  private ProductosServices servicio;

  @Autowired
  private CategoriasServices servicioCategorias;

  @GetMapping
  public String listarTodo(Model model) {
    prepararVista(model);
    return VISTA_LISTAR;
  }

  @PostMapping("/agregar")
  public String agregar(@Valid @ModelAttribute("productoAgregarDto") ProductoAgregarDTO productoAgregarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      model.addAttribute("productoAgregarDto", productoAgregarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");
      return VISTA_LISTAR;
    }

    try {
      servicio.createProducto(productoAgregarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);
      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      model.addAttribute("productoAgregarDto", productoAgregarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");
      return VISTA_LISTAR;
    }

    return REDIRECCIONAR;
  }

  @PostMapping("/editar")
  public String editar(@Valid @ModelAttribute("productoEditarDto") ProductoEditarDTO productoEditarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      model.addAttribute("productoEditarDto", productoEditarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");
      return VISTA_LISTAR;
    }

    try {
      servicio.updateProducto(productoEditarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);
      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      model.addAttribute("productoEditarDto", productoEditarDto);
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");
      return VISTA_LISTAR;
    }

    return REDIRECCIONAR;
  }

@PostMapping("/eliminar")
  public String eliminar(@ModelAttribute ProductoEliminarDTO productoEliminarDto, RedirectAttributes redirectAttributes) {
    try {
      servicio.deleteProducto(productoEliminarDto.getIdProducto());
      redirectAttributes.addFlashAttribute("mensajeExito", "Producto eliminado correctamente.");
    } catch (DataIntegrityViolationException e) {
      // Este error salta si el producto está en Pedidos, Carritos o Inventario
      redirectAttributes.addFlashAttribute("mensajeError", "No se puede eliminar el producto porque tiene registros asociados (Pedidos, Inventario o Carritos).");
    } catch (Exception e) {
      // Cualquier otro error imprevisto
      redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error inesperado al intentar eliminar.");
    }

    return REDIRECCIONAR;
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
  }
}
