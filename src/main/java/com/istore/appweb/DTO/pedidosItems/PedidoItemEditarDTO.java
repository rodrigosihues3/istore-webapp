package com.istore.appweb.DTO.pedidosItems;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoItemEditarDTO {

  @NotNull(message = "El ID del item es obligatorio")
  private Integer idPedidoItem;

  @NotNull(message = "{NotNull.productos.idProducto}")
  private Integer idProducto;

  @NotNull(message = "{NotNull.pedidosItems.cantidad}")
  @Positive(message = "{Positive.pedidosItems.cantidad}")
  private Integer cantidad;

  @NotNull(message = "{NotNull.pedidosItems.precio}")
  @Positive(message = "{Positive.pedidosItems.precio}")
  private BigDecimal precio; // Precio editable y congelado del producto

  // El campo 'total' lo vuelve a calcular el servicio
}