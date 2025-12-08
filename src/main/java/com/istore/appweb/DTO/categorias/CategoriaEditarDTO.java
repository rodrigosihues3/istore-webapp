package com.istore.appweb.DTO.categorias;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

  @NotNull(message = "{NotNull.categorias.nombre}")
  @NotBlank(message = "{NotBlank.categorias.nombre}")
  @Pattern(regexp = "^(?=.*\\p{L}.*\\p{L})\\s*[\\p{L}\\s]+\\s*$", message = "{Pattern.categorias.nombre}")
  private String nombre;
}
