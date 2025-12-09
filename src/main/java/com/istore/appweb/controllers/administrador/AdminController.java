package com.istore.appweb.controllers.administrador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

  private final String CARPETA_BASE = "administrador/";
  private final String VISTA_INICIO = CARPETA_BASE + "index-administrador";
  // private final String REDIRECCIONAR = "redirect:/admin/";

  @GetMapping
  public String inicio() {
    return VISTA_INICIO;
  }

}
