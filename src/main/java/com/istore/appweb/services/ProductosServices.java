package com.istore.appweb.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.istore.appweb.DTO.productos.ProductoAgregarDTO;
import com.istore.appweb.DTO.productos.ProductoEditarDTO;
import com.istore.appweb.configs.Utilidades;
import com.istore.appweb.entities.Productos;
import com.istore.appweb.repositories.CategoriasRepository;
import com.istore.appweb.repositories.ProductosRepository;

@Service
public class ProductosServices {

  @Autowired
  private ProductosRepository repositorio;

  @Autowired
  private CategoriasRepository repositorioCategorias;

  public List<Productos> getProductos() {
    return repositorio.findAll(Sort.by(Sort.Direction.DESC, "idProducto"));
  }

  public Productos getProductoById(Integer id) {
    return repositorio.findById(id).get();
  }

  public Optional<Productos> getProductoBySku(String sku) {
    return repositorio.findBySku(sku);
  }

  public Optional<Productos> getProductoByNombre(String nombre) {
    return repositorio.findByNombre(nombre);
  }

  public Productos createProducto(ProductoAgregarDTO productoDTO) {
    if (repositorio.existsBySku(productoDTO.getSku().trim())) {
      throw new IllegalArgumentException("sku:Este código SKU ya existe, intente con otro.");
    }
    if (repositorio.existsByNombre(productoDTO.getNombre().trim())) {
      throw new IllegalArgumentException("nombre:Este nombre de producto ya existe, intente con otro.");
    }

    Productos producto = mapearYNormalizar(
        productoDTO.getSku(),
        productoDTO.getNombre(),
        productoDTO.getDescripcion(),
        productoDTO.getPrecio());

    producto.setCategoria(repositorioCategorias.findById(productoDTO.getIdCategoria()).get());
    producto.setFechaCreacion(LocalDateTime.now());

    return repositorio.save(producto);
  }

  public Productos updateProducto(ProductoEditarDTO productoDTO) {
    Productos productoExistente = repositorio.findById(productoDTO.getIdProducto()).get();

    if (!java.util.Objects.equals(productoExistente.getSku(), productoDTO.getSku().trim())
        && repositorio.existsBySku(productoDTO.getSku().trim())) {
      throw new IllegalArgumentException("sku:El nuevo código SKU ya existe, intente con otro.");
    }
    if (!java.util.Objects.equals(productoExistente.getNombre(), productoDTO.getNombre().trim())
        && repositorio.existsByNombre(productoDTO.getNombre().trim())) {
      throw new IllegalArgumentException("nombre:El nuevo nombre de producto ya existe, intente con otro.");
    }

    productoExistente.setSku(productoDTO.getSku().trim());
    productoExistente.setNombre(Utilidades.normalizarTexto(productoDTO.getNombre()));
    productoExistente.setDescripcion(Utilidades.normalizarTexto(productoDTO.getDescripcion()));
    productoExistente.setPrecio(productoDTO.getPrecio());
    productoExistente.setCategoria(repositorioCategorias.findById(productoDTO.getIdCategoria()).get());
    productoExistente.setFechaCreacion(LocalDateTime.now());

    return repositorio.save(productoExistente);
  }

  public void deleteProducto(Integer id) {
    repositorio.deleteById(id);
  }

  // Sobrecarga para agregar producto directo (sin DTO)
  public Productos createProducto(Productos producto) {
    if (repositorio.existsBySku(producto.getSku().trim())) {
      throw new IllegalArgumentException("sku:Este código SKU ya existe, intente con otro.");
    }
    if (repositorio.existsByNombre(producto.getNombre().trim())) {
      throw new IllegalArgumentException("nombre:Este nombre de producto ya existe, intente con otro.");
    }

    producto.setSku(producto.getSku().trim());
    producto.setNombre(Utilidades.normalizarTexto(producto.getNombre()));
    producto.setDescripcion(Utilidades.normalizarTexto(producto.getDescripcion()));
    producto.setFechaCreacion(LocalDateTime.now());

    return repositorio.save(producto);
  }

  private Productos mapearYNormalizar(String sku, String nombre, String descripcion, BigDecimal precio) {
    Productos producto = new Productos();

    producto.setSku(sku.trim());
    producto.setNombre(Utilidades.normalizarTexto(nombre));
    producto.setDescripcion(Utilidades.normalizarTexto(descripcion));
    producto.setPrecio(precio);

    return producto;
  }
}
