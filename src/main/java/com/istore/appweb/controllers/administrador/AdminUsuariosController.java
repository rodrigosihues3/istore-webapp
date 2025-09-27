package com.istore.appweb.controllers.administrador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.istore.appweb.entities.Usuarios;
import com.istore.appweb.services.UsuariosServices;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuariosController {

  private final String CARPETA_BASE = "tablasBD/";
  private final String VISTA_LISTAR = CARPETA_BASE + "usuarios";
  private final String REDIRECCIONAR = "redirect:/admin/usuarios";

  @Autowired
  private UsuariosServices usuariosServices;

  @GetMapping
  public String listarUsuarios(Model model) {
    List<Usuarios> usuarios = usuariosServices.getUsuarios();

    model.addAttribute("usuarios", usuarios);
    model.addAttribute("usuario", new Usuarios());

    return VISTA_LISTAR;
  }

  @PostMapping("/agregar")
  public String agregarUsuario(@ModelAttribute Usuarios usuario) {
    usuariosServices.createUsuario(usuario);

    return REDIRECCIONAR;
  }

}
