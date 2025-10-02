package com.istore.appweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mi-cuenta")
public class UsuariosController {

    private final String VISTA_MI_CUENTA = "account";
    private final String CARPETA_BASE = "clientes/";
    private final String VISTA_PEDIDOS = CARPETA_BASE + "pedidos";
    private final String REDIRECCIONAR = "redirect:/";

    @GetMapping
    public String miCuenta() {
        return VISTA_MI_CUENTA; // Retorna la vista index.html en templates/
    }

    @GetMapping("/pedidos")
    public String getPedidos(@RequestParam String param) {
        return VISTA_PEDIDOS;
    }

}
