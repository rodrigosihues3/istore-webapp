package com.istore.appweb.controllers.clientes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.istore.appweb.DTO.usuarios.ClienteActualizarContrasenaDTO;
import com.istore.appweb.DTO.usuarios.ClienteEditarDTO;
import com.istore.appweb.configs.security.UsuariosDetails;
import com.istore.appweb.entities.Usuarios;
import com.istore.appweb.services.ClientesServices;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/mi-cuenta")
public class ClientesControllers {

  private final String CARPETA_BASE = "clientes/";
  private final String VISTA_MI_CUENTA = CARPETA_BASE + "account";
  private final String REDIRECCIONAR_CUENTA = "redirect:/mi-cuenta";

  @Autowired
  private ClientesServices servicio;

  @GetMapping
  public String miCuenta(@AuthenticationPrincipal UsuariosDetails userDetails, Model model) {
    prepararVista(model, userDetails);

    return VISTA_MI_CUENTA;
  }

  @PostMapping("/actualizar")
  public String actualizarMiCuenta(@Valid @ModelAttribute("clienteDto") ClienteEditarDTO clienteDto,
      BindingResult result,
      @AuthenticationPrincipal UsuariosDetails userDetails,
      RedirectAttributes redirectAttributes,
      Model model) {
    if (result.hasErrors()) {
      prepararVista(model, userDetails);

      return VISTA_MI_CUENTA;
    }

    try {
      Usuarios cliente = userDetails.getUsuario();
      clienteDto.setIdUsuario(cliente.getIdUsuario());

      servicio.updateUsuario(clienteDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      }

      prepararVista(model, userDetails);

      return VISTA_MI_CUENTA;
    }

    redirectAttributes.addFlashAttribute("successMyAccount", "Tu datos fueron actualizados correctamente.");
    return REDIRECCIONAR_CUENTA;
  }

  @PostMapping("/cambiar-password")
  public String cambiarContraseña(
      @Valid @ModelAttribute("clienteContrasenaDto") ClienteActualizarContrasenaDTO clienteContrasenaDto,
      BindingResult result,
      @AuthenticationPrincipal UsuariosDetails userDetails,
      RedirectAttributes redirectAttributes,
      Model model) {
    if (result.hasErrors()) {
      prepararVista(model, userDetails);

      return VISTA_MI_CUENTA;
    }

    try {
      Usuarios cliente = servicio.getUsuarioById(userDetails.getUsuario().getIdUsuario());
      clienteContrasenaDto.setIdUsuario(cliente.getIdUsuario());

      servicio.updateContrasena(clienteContrasenaDto);
    } catch (IllegalArgumentException e) {
      String[] partes = e.getMessage().split(":", 2);

      if (partes.length == 2) {
        result.rejectValue(partes[0], "error." + partes[0], partes[1]);
      } else if (e.getMessage() != null && !e.getMessage().isBlank()) {
        model.addAttribute("errorFormNewPassword", e.getMessage());
      }

      prepararVista(model, userDetails);

      return VISTA_MI_CUENTA;
    }

    redirectAttributes.addFlashAttribute("successFormNewPassword", "Tu contraseña fue actualizada correctamente.");
    return REDIRECCIONAR_CUENTA;
  }

  private void prepararVista(Model model, @AuthenticationPrincipal UsuariosDetails userDetails) {
    if (userDetails == null) {
      // opcional: redirect al login o lanzar excepción
      return;
    }

    // Obtener el cliente actual desde la sesión
    Usuarios cliente = servicio.getUsuarioById(userDetails.getUsuario().getIdUsuario());

    // Solo agregar el DTO si no existe (no sobreescribir el que viene con errores)
    if (!model.containsAttribute("clienteDto")) {
      ClienteEditarDTO clienteDto = new ClienteEditarDTO();
      clienteDto.setIdUsuario(cliente.getIdUsuario());
      clienteDto.setNombres(cliente.getNombres());
      clienteDto.setApellidos(cliente.getApellidos());
      clienteDto.setEmail(cliente.getEmail());
      clienteDto.setNombreUsuario(cliente.getNombreUsuario());
      clienteDto.setDni(cliente.getDni());
      clienteDto.setTelefono(cliente.getTelefono());
      clienteDto.setDireccion(cliente.getDireccion());

      model.addAttribute("clienteDto", clienteDto);
    }

    // Asegurarse de que el DTO de contraseña también exista
    if (!model.containsAttribute("clienteContrasenaDto")) {
      model.addAttribute("clienteContrasenaDto", new ClienteActualizarContrasenaDTO());
    }
  }

}
