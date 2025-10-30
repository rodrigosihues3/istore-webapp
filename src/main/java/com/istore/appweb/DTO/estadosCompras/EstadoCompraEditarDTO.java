package com.istore.appweb.DTO.estadosCompras;

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
public class EstadoCompraEditarDTO {

  private Integer idEstadoCompra;

  @NotNull(message = "{NotNull.colores.nombre}")
  @NotBlank(message = "{NotBlank.colores.nombre}")
  @Pattern(regexp = "^(?=.*\\p{L}.*\\p{L})\\s*[\\p{L}\\s]+\\s*$", message = "{Pattern.colores.nombre}")
  private String nombre;

}
