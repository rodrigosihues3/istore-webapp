package com.istore.appweb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.istore.appweb.DTO.usuarios.ClienteRegistrarDTO;
import com.istore.appweb.entities.Usuarios;
import com.istore.appweb.services.RolesServices;
import com.istore.appweb.services.UsuariosServices;

@Controller
public class Login {

    @Autowired
    private UsuariosServices servicio;

    @Autowired
    private RolesServices servicioRoles;

    @GetMapping("/iniciar-sesion")
    public String login() {
        return "login"; // Retorna la vista index.html en templates/
    }

    @GetMapping("/registrarse")
    public String register() {
        return "register"; // Retorna la vista index.html en templates/
    }

    @PostMapping("/registrarse")
    public String crearRegistro(@ModelAttribute ClienteRegistrarDTO clienteDto, Model model) {
        // Validar contraseñas
        if (!clienteDto.getPassword().equals(clienteDto.getConfirmPassword())) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "register";
        }

        Usuarios usuario = new Usuarios();

        usuario.setNombres(clienteDto.getNombres().toUpperCase());
        usuario.setApellidos(clienteDto.getApellidos().toUpperCase());
        usuario.setEmail(clienteDto.getEmail().toUpperCase());
        usuario.setNombreUsuario(clienteDto.getNombreUsuario().toUpperCase());
        usuario.setPassword(clienteDto.getPassword());
        usuario.setRol(servicioRoles.getByNombre("CLIENTE"));

        servicio.createUsuario(usuario);

        return "redirect:/iniciar-sesion";
    }

}
