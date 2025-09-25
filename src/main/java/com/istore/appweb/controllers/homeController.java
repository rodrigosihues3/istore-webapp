package com.istore.appweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class homeController {
    
    @GetMapping("/")
    public String home() {        
        return "index"; // Retorna la vista index.html en templates/
    }
    
    @GetMapping("/tiendas")
    public String tiendas() {        
        return "tiendas"; // Retorna la vista index.html en templates/
    }

    @GetMapping("/nosotros")
    public String nosotros() {        
        return "nosotros"; // Retorna la vista index.html en templates/
    }

    @GetMapping("/account")
    public String account() {        
        return "account"; // Retorna la vista index.html en templates/
    }
}
