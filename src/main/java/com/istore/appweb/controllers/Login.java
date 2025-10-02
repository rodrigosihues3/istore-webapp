package com.istore.appweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Login {

    @GetMapping("/iniciar-sesion")
    public String login() {
        return "login.html"; // Retorna la vista index.html en templates/
    }

    @GetMapping("/registrarse")
    public String register() {
        return "register.html"; // Retorna la vista index.html en templates/
    }

    
}
