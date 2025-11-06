package com.istore.appweb.DTO.colores;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ColorEliminarDTO {

  @NotNull(message = "{NotNull.colores.idColor}")
  private Integer idColor;

}
