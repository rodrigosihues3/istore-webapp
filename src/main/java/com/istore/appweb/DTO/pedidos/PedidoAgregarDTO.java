package com.istore.appweb.DTO.pedidos;

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
public class PedidoAgregarDTO {

  @NotNull(message = "{NotNull.pedidos.nombre}")
  @NotBlank(message = "{NotBlank.pedidos.nombre}")
  @Pattern(regexp = "^(?=.*\\p{L}.*\\p{L})\\s*[\\p{L}\\s]+\\s*$", message = "{Pattern.estadosCompras.nombre}")
  private String nombre;

}
