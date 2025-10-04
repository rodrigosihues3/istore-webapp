package com.istore.appweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente/pagar-carrito")
public class CarritosController {

  @GetMapping
  public String getPago() {
    return "clientes/pago";
  }

}
