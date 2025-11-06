package com.istore.appweb.DTO.pedidos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoEliminarDTO {

  @NotNull(message = "{NotNull.pedidos.idPedido}")
  private Integer idPedido;

}
