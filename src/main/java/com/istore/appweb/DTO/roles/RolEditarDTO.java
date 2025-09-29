package com.istore.appweb.DTO.roles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RolEditarDTO {

  private Integer idRol;

  private String nombre;

  private Integer nivel;

}
