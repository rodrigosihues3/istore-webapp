package com.istore.appweb.DTO.usuarios;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteActualizarContrasenaDTO {

  private Integer idUsuario;

  @NotBlank(message = "La contraseña ingresada es incorrecta.")
  private String password;

  @NotNull(message = "{NotNull.usuarios.password}")
  @NotBlank(message = "{NotBlank.usuarios.password}")
  @Size(min = 8, message = "{Size.usuarios.password}")
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$", message = "{Pattern.usuarios.password}")
  private String newPassword;

  @NotNull(message = "{NotNull.usuarios.confirmPassword}")
  @NotBlank(message = "{NotBlank.usuarios.confirmPassword}")
  private String confirmPassword;

}
