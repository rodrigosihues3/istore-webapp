package com.istore.appweb.DTO.usuarios;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioAgregarDTO {

  private String nombres;

  private String apellidos;

  private String email;

  private String nombreUsuario;

  private String password;

  private String dni;

  private String telefono;

  private String direccion;

  private Integer idRol;

}
