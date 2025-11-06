package com.istore.appweb.DTO.usuarios;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEliminarDTO {

  @NotNull(message = "{NotNull.usuarios.idUsuario}")
  private Integer idUsuario;

}
