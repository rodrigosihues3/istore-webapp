package com.istore.appweb.DTO.metodosPagos;

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
public class MetodoPagoAgregarDTO {

  @NotNull(message = "{NotNull.metodosPagos.nombre}")
  @NotBlank(message = "{NotBlank.metodosPagos.nombre}")
  @Pattern(regexp = "^(?=.*\\p{L}.*\\p{L})\\s*[\\p{L}\\s]+\\s*$", message = "{Pattern.estadosPagos.nombre}")
  private String nombre;

}
