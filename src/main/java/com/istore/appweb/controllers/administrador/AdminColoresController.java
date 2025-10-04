package com.istore.appweb.controllers.administrador;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.istore.appweb.DTO.colores.ColorEditarDTO;
import com.istore.appweb.DTO.colores.ColorEliminarDTO;
import com.istore.appweb.entities.Colores;
import com.istore.appweb.services.ColoresServices;


@Controller
@RequestMapping("/admin/colores")
public class AdminColoresController {

  private final String CARPETA_BASE = "tablasBD/";
  private final String VISTA_LISTAR = CARPETA_BASE + "colores";
  private final String REDIRECCIONAR = "redirect:/admin/colores";

  @Autowired
  private ColoresServices servicio;

  @GetMapping
  public String listarTodo(Model model) {
    List<Colores> colores = servicio.getColores();
    Colores color = new Colores();

    model.addAttribute("colores", colores);
    model.addAttribute("color", color);

    return VISTA_LISTAR;
  }

  @PostMapping("/agregar")
  public String agregar(@ModelAttribute Colores color) {
    color.setNombre(color.getNombre().toUpperCase());

    servicio.createColor(color);

    return REDIRECCIONAR;
  }

  @PostMapping("/editar")
  public String editar(@ModelAttribute ColorEditarDTO colorDto) {
    Colores colorExistente = servicio.getColorById(colorDto.getIdColor());

    colorExistente.setNombre(colorDto.getNombre().toUpperCase());

    colorExistente.setFechaCreacion(LocalDateTime.now());

    servicio.updateColor(colorExistente);

    return REDIRECCIONAR;
  }

  @PostMapping("/eliminar")
  public String eliminar(@ModelAttribute ColorEliminarDTO colorDTO) {
    servicio.deleteById(colorDTO.getIdColor());

    return REDIRECCIONAR;
  }

}
