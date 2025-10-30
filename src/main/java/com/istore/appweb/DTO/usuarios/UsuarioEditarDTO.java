package com.istore.appweb.DTO.usuarios;

import jakarta.validation.constraints.Email;
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
public class UsuarioEditarDTO {

  private Integer idUsuario;

  @NotNull(message = "{NotNull.usuarios.nombres}")
  @NotBlank(message = "{NotBlank.usuarios.nombres}")
  private String nombres;

  @NotNull(message = "{NotNull.usuarios.apellidos}")
  @NotBlank(message = "{NotBlank.usuarios.apellidos}")
  private String apellidos;

  @NotNull(message = "{NotNull.usuarios.email}")
  @NotBlank(message = "{NotBlank.usuarios.email}")
  @Email(message = "{Email.usuarios.email}")
  private String email;

  @NotNull(message = "{NotNull.usuarios.nombreUsuario}")
  @NotBlank(message = "{NotBlank.usuarios.nombreUsuario}")
  @Size(min = 4, message = "{Size.usuarios.nombreUsuario}")
  @Pattern(regexp = "^[a-zA-Z0-9_.-]*$", message = "{Pattern.usuarios.nombreUsuario}")
  private String nombreUsuario;

  @Pattern(regexp = "^$|^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$", message = "{Pattern.usuarios.password}")
  private String password;

  @Pattern(regexp = "^$|^\\d{8}$", message = "{Pattern.usuarios.dni}")
  private String dni;

  @Pattern(regexp = "^$|^\\d{9}$", message = "{Pattern.usuarios.telefono}")
  private String telefono;

  private String direccion;

  private Integer idRol;
}
