package com.istore.appweb.DTO.usuarios;

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

  private String password;

  private String newPassword;

  private String confirmPassword;

}
