package com.istore.appweb.models;

import java.math.BigDecimal;

import com.istore.appweb.entities.Productos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarritoItem {

  private Productos producto;
  private Integer cantidad;

  // Método corregido para BigDecimal
  public BigDecimal getTotal() {
    if (producto.getPrecio() == null || cantidad == null) {
      return BigDecimal.ZERO;
    }
    return producto.getPrecio().multiply(new BigDecimal(cantidad));
  }
}
