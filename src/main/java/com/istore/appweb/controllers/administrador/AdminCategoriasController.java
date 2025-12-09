package com.istore.appweb.controllers.administrador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.istore.appweb.DTO.categorias.CategoriaAgregarDTO;
import com.istore.appweb.DTO.categorias.CategoriaEditarDTO;
import com.istore.appweb.DTO.categorias.CategoriaEliminarDTO;
import com.istore.appweb.services.CategoriasServices;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/categorias")
public class AdminCategoriasController {

  private final String FRAGMENTO = "tablaCategorias";
  private final String VISTA_FRAGMENTO = "administrador/tablasBD/categorias :: " + FRAGMENTO;

  @Autowired
  private CategoriasServices servicio;

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
  public String agregar(@Valid @ModelAttribute("categoriaAgregarDto") CategoriaAgregarDTO categoriaAgregarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_FRAGMENTO;
    }

    try {
      servicio.createCategoria(categoriaAgregarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalAgregar");

      return VISTA_FRAGMENTO;
    }

    model.addAttribute("categoriaAgregarDto", new CategoriaAgregarDTO());
    prepararVista(model);

    return VISTA_FRAGMENTO;
  }

  @PostMapping("/editar")
  public String editar(@Valid @ModelAttribute("categoriaEditarDto") CategoriaEditarDTO categoriaEditarDto,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_FRAGMENTO;
    }

    try {
      servicio.updateCategoria(categoriaEditarDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      prepararVista(model);
      model.addAttribute("mostrarModal", "#modalEditar");

      return VISTA_FRAGMENTO;
    }

    prepararVista(model);

    return VISTA_FRAGMENTO;
  }

  @PostMapping("/eliminar")
  public String eliminar(@ModelAttribute CategoriaEliminarDTO categoriaDTO, Model model) {
    servicio.deleteById(categoriaDTO.getIdCategoria());
    prepararVista(model);

    return VISTA_FRAGMENTO;
  }

  private void prepararVista(Model model) {
    if (!model.containsAttribute("categoriaAgregarDto")) {
      model.addAttribute("categoriaAgregarDto", new CategoriaAgregarDTO());
    }
    if (!model.containsAttribute("categoriaEditarDto")) {
      model.addAttribute("categoriaEditarDto", new CategoriaEditarDTO());
    }

    model.addAttribute("categorias", servicio.getCategorias());
  }
}
