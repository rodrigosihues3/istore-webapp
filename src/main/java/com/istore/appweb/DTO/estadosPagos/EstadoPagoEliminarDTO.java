package com.istore.appweb.DTO.estadosPagos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstadoPagoEliminarDTO {

  @NotNull(message = "{NotNull.estadosPagos.idEstadoPago}")
  private Integer idEstadoPago;

}
