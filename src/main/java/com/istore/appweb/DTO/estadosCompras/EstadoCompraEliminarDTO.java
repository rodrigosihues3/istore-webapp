package com.istore.appweb.DTO.estadosCompras;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstadoCompraEliminarDTO {

  @NotNull(message = "{NotNull.estadosCompras.idEstadoCompra}")
  private Integer idEstadoCompra;

}
