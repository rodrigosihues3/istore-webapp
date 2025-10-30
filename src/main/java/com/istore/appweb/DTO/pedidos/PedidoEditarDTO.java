package com.istore.appweb.DTO.pedidos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class PedidoEditarDTO {

  private BigDecimal total;

  private Integer metodoPago;
  private Integer tipoComprobante;
  private Integer usuario;
  private Integer estadoCompra;

  private LocalDateTime fechaActualizacion;

}
