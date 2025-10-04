package com.istore.appweb.controllers.clientes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

@Controller
@RequestMapping("/mi-cuenta")
public class ClientesControllers {

  private final String CARPETA_BASE = "clientes/";
  private final String VISTA_MI_CUENTA = CARPETA_BASE + "account";
  private final String VISTA_PEDIDOS = CARPETA_BASE + "pedidos";
  private final String REDIRECCIONAR_CUENTA = "redirect:/mi-cuenta";

  @Autowired
  private ClientesServices servicio;

  @Autowired
  private PasswordEncoder encoder;

  @GetMapping
  public String miCuenta(@AuthenticationPrincipal UsuariosDetails userDetails, Model model) {
    Usuarios cliente = servicio.getUsuarioById(userDetails.getUsuario().getIdUsuario());
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
    model.addAttribute("clienteContrasenaDto", new ClienteActualizarContrasenaDTO());

    return VISTA_MI_CUENTA;
  }

  @PostMapping("/actualizar")
  public String actualizarMiCuenta(@ModelAttribute ClienteEditarDTO clienteDto,
      @AuthenticationPrincipal UsuariosDetails userDetails,
      RedirectAttributes redirectAttributes,
      Model model) {
    Usuarios cliente = userDetails.getUsuario();
    clienteDto.setIdUsuario(cliente.getIdUsuario());

    servicio.updateUsuario(clienteDto);

    redirectAttributes.addFlashAttribute("success", "Tu datos fueron actualizados correctamente.");
    return REDIRECCIONAR_CUENTA;
  }

  @PostMapping("/cambiar-password")
  public String cambiarContraseña(@ModelAttribute ClienteActualizarContrasenaDTO clienteContrasenaDto,
      @AuthenticationPrincipal UsuariosDetails userDetails, RedirectAttributes redirectAttributes, Model model) {
    Usuarios cliente = servicio.getUsuarioById(userDetails.getUsuario().getIdUsuario());
    clienteContrasenaDto.setIdUsuario(cliente.getIdUsuario());

    // Validar que no haya campos vacíos
    if (clienteContrasenaDto.getPassword() == null || clienteContrasenaDto.getPassword().isBlank() ||
        clienteContrasenaDto.getNewPassword() == null || clienteContrasenaDto.getNewPassword().isBlank() ||
        clienteContrasenaDto.getConfirmPassword() == null || clienteContrasenaDto.getConfirmPassword().isBlank()) {

      redirectAttributes.addFlashAttribute("error", "Debes completar todos los campos.");
      return REDIRECCIONAR_CUENTA;
    }

    // Validar contraseña actual
    if (!encoder.matches(clienteContrasenaDto.getPassword(), cliente.getPassword())) {
      redirectAttributes.addFlashAttribute("error", "La contraseña actual es incorrecta.");
      return REDIRECCIONAR_CUENTA;
    }

    // Validar coincidencia de la nueva contraseña
    if (!clienteContrasenaDto.getNewPassword().equals(clienteContrasenaDto.getConfirmPassword())) {
      redirectAttributes.addFlashAttribute("error", "La nueva contraseña y su confirmación no coinciden.");
      return REDIRECCIONAR_CUENTA;
    }

    servicio.updateContrasena(clienteContrasenaDto);

    redirectAttributes.addFlashAttribute("success", "Tu contraseña fue actualizada correctamente.");
    return REDIRECCIONAR_CUENTA;
  }

  @GetMapping("/pedidos")
  public String getPedidos() {
    return VISTA_PEDIDOS;
  }

}
