package com.istore.appweb.DTO.estadosPagos;

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
public class EstadoPagoAgregarDTO {

  @NotNull(message = "{NotNull.estadosPagos.nombre}")
  @NotBlank(message = "{NotBlank.estadosPagos.nombre}")
  @Pattern(regexp = "^(?=.*\\p{L}.*\\p{L})\\s*[\\p{L}\\s]+\\s*$", message = "{Pattern.estadosPagos.nombre}")
  private String nombre;

}
