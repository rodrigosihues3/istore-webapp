package com.istore.appweb.DTO.categorias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaEditarDTO {

  private Integer idCategoria;

  private String nombre;
}
