package com.istore.appweb.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.istore.appweb.entities.Categorias;
import com.istore.appweb.services.CategoriasServices;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalController {

  @Autowired
  private CategoriasServices categoriasService;

  @ModelAttribute("categoriasMenu")
  public List<Categorias> populateCategorias() {
    return categoriasService.getCategorias();
  }

  @ModelAttribute("currentPath")
  public String currentPath(HttpServletRequest request) {
    return request.getRequestURI();
  }

}
