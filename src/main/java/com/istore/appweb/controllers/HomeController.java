package com.istore.appweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping
    public String home() {
        return "index";
    }

    @GetMapping("/tiendas")
    public String tiendas() {
        return "tiendas";
    }

    @GetMapping("/nosotros")
    public String nosotros() {
        return "nosotros";
    }

}
