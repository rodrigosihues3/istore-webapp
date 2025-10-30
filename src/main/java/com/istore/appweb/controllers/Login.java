package com.istore.appweb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.istore.appweb.DTO.usuarios.ClienteRegistrarDTO;
import com.istore.appweb.services.UsuariosServices;

import jakarta.validation.Valid;

@Controller
public class Login {

    @Autowired
    private UsuariosServices servicio;

    @GetMapping("/iniciar-sesion")
    public String login() {
        return "login"; // Retorna la vista index.html en templates/
    }

    @GetMapping("/registrarse")
    public String register(Model model) {
        model.addAttribute("clienteDto", new ClienteRegistrarDTO());

        return "register"; // Retorna la vista index.html en templates/
    }

    @PostMapping("/registrarse")
    public String crearRegistro(@Valid @ModelAttribute("clienteDto") ClienteRegistrarDTO clienteDto,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        // Validar que las contraseñas sean iguales
        if (!clienteDto.getPassword().equals(clienteDto.getConfirmPassword())) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "register";
        }

        try {
            servicio.createUsuario(clienteDto);
        } catch (IllegalArgumentException e) {
            String[] partes = e.getMessage().split(":", 2);

            if (partes.length == 2) {
                result.rejectValue(partes[0], "error." + partes[0], partes[1]);
            }

            return "register";
        }

        return "redirect:/iniciar-sesion";
    }

}
