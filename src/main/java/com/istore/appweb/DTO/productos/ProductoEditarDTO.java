package com.istore.appweb.DTO.productos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductoEditarDTO {

  private Integer idProducto;

  @NotNull(message = "{NotNull.productos.sku}")
  @NotBlank(message = "{NotBlank.productos.sku}")
  @Size(min = 4, message = "{Size.productos.sku}")
  private String sku;

  @NotNull(message = "{NotNull.productos.nombre}")
  @NotBlank(message = "{NotBlank.productos.nombre}")
  private String nombre;

  @NotNull(message = "{NotNull.productos.descripcion}")
  @NotBlank(message = "{NotBlank.productos.descripcion}")
  private String descripcion;

  @NotNull(message = "{NotNull.productos.precio}")
  @Positive(message = "{Positive.productos.precio}")
  private BigDecimal precio;

  private Integer idCategoria;
}
