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
public class MetodoPagoEditarDTO {

  @NotNull(message = "{NotNull.metodosPagos.idMetodoPago}")
  private Integer idMetodoPago;

  @NotNull(message = "{NotNull.metodosPagos.nombre}")
  @NotBlank(message = "{NotBlank.metodosPagos.nombre}")
  @Pattern(regexp = "^(?=.*\\p{L}.*\\p{L})\\s*[\\p{L}\\s]+\\s*$", message = "{Pattern.metodosPagos.nombre}")
  private String nombre;

}
