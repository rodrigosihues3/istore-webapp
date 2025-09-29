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

import com.istore.appweb.DTO.roles.RolEditarDTO;
import com.istore.appweb.DTO.roles.RolEliminarDTO;
import com.istore.appweb.entities.Roles;
import com.istore.appweb.services.RolesServices;

@Controller
@RequestMapping("/admin/roles")
public class AdminRolesController {

  private final String CARPETA_BASE = "tablasBD/";
  private final String VISTA_LISTAR = CARPETA_BASE + "roles";
  private final String REDIRECCIONAR = "redirect:/admin/roles";

  @Autowired
  private RolesServices servicio;

  @GetMapping
  public String listarTodo(Model model) {
    List<Roles> roles = servicio.getRoles();
    Roles rol = new Roles();
    rol.setNivel(0);

    model.addAttribute("roles", roles);
    model.addAttribute("rol", rol);

    return VISTA_LISTAR;
  }

  @PostMapping("/agregar")
  public String agregar(@ModelAttribute Roles rol) {
    rol.setNombre(rol.getNombre().toUpperCase());

    servicio.createRol(rol);

    return REDIRECCIONAR;
  }

  @PostMapping("/editar")
  public String editar(@ModelAttribute RolEditarDTO rolDto) {
    Roles rolExistente = servicio.getRolById(rolDto.getIdRol());

    rolExistente.setNombre(rolDto.getNombre().toUpperCase());
    rolExistente.setNivel(rolDto.getNivel());

    rolExistente.setFechaCreacion(LocalDateTime.now());

    servicio.updateRol(rolExistente);

    return REDIRECCIONAR;
  }

  @PostMapping("/eliminar")
  public String eliminar(@ModelAttribute RolEliminarDTO rolDTO) {
    servicio.deleteRol(rolDTO.getIdRol());

    return REDIRECCIONAR;
  }

}
