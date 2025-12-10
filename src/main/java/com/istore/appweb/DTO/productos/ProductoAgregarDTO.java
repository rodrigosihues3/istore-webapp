package com.istore.appweb.DTO.productos;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ProductoAgregarDTO {

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

  @NotNull(message = "El stock es obligatorio")
  @Min(value = 0, message = "El stock no puede ser negativo")
  private Integer stock;

  @NotNull(message = "Debes seleccionar un color")
  private Integer idColor;

  private Integer idCategoria;
  private MultipartFile imagen;
}
