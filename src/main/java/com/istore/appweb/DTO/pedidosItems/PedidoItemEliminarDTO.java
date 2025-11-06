package com.istore.appweb.DTO.pedidosItems;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoItemEliminarDTO {

  @NotNull(message = "{NotNull.pedidosItems.idPedidoItem}")
  private Integer idPedidoItem;
  
}