package com.istore.appweb.DTO.tiposComprobantes;

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
public class TipoComprobanteEditarDTO {

  @NotNull(message = "{NotNull.tiposComprobantes.idTipoComprobante}") 
  private Integer idTipoComprobante;

  @NotNull(message = "{NotNull.tiposComprobantes.nombre}")
  @NotBlank(message = "{NotBlank.tiposComprobantes.nombre}")
  @Pattern(regexp = "^(?=.*\\p{L}.*\\p{L})\\s*[\\p{L}\\s]+\\s*$", message = "{Pattern.tiposComprobantes.nombre}")
  private String nombre;

}
