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
public class PedidoAgregarDTO {

  @NotNull(message = "{NotNull.usuarios.idUsuario}")
  private Integer idUsuario;

  @NotNull(message = "{NotNull.metodosPagos.idMetodoPago}")
  private Integer idMetodoPago;

  @NotNull(message = "{NotNull.tiposComprobantes.idTipoComprobante}")
  private Integer idTipoComprobante;

  @NotNull(message = "{NotNull.estadosCompras.idEstadoCompra}")
  private Integer idEstadoCompra;

}
