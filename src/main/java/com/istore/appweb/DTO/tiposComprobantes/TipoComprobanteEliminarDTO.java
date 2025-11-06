package com.istore.appweb.DTO.tiposComprobantes;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipoComprobanteEliminarDTO {

  @NotNull(message = "{NotNull.tiposComprobantes.idTipoComprobante}")
  private Integer idTipoComprobante;

}
