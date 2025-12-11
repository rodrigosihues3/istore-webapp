package com.istore.appweb.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.istore.appweb.entities.Productos;
import com.istore.appweb.models.CarritoItem;
import com.istore.appweb.repositories.ProductosRepository;

@Service
@SessionScope
public class CarritoService {

  @Autowired
  private ProductosRepository productoRepo;

  private List<CarritoItem> items = new ArrayList<>();

  public void agregarItem(Integer idProducto, Integer cantidad) {
    Optional<CarritoItem> existente = items.stream()
        .filter(i -> i.getProducto().getIdProducto().equals(idProducto))
        .findFirst();

    if (existente.isPresent()) {
      CarritoItem item = existente.get();
      item.setCantidad(item.getCantidad() + cantidad);
    } else {
      Productos producto = productoRepo.findById(idProducto).orElse(null);
      if (producto != null) {
        items.add(new CarritoItem(producto, cantidad));
      }
    }
  }

  public void actualizarCantidad(Integer idProducto, Integer nuevaCantidad) {
    if (nuevaCantidad <= 0) {
      removerItem(idProducto); // Si pone 0, se borra
      return;
    }

    items.stream()
        .filter(i -> i.getProducto().getIdProducto().equals(idProducto))
        .findFirst()
        .ifPresent(item -> item.setCantidad(nuevaCantidad));
  }

  public void removerItem(Integer idProducto) {
    items.removeIf(i -> i.getProducto().getIdProducto().equals(idProducto));
  }

  public List<CarritoItem> getItems() {
    return items;
  }

  public BigDecimal getTotalGeneral() {
    return items.stream()
        .map(CarritoItem::getTotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public Integer getCantidadItems() {
    return items.stream().mapToInt(CarritoItem::getCantidad).sum();
  }

  public void limpiarCarrito() {
    items.clear();
  }
}
