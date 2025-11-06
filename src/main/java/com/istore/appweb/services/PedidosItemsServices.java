package com.istore.appweb.services;

import com.istore.appweb.DTO.pedidosItems.PedidoItemAgregarDTO;
import com.istore.appweb.DTO.pedidosItems.PedidoItemEditarDTO;
import com.istore.appweb.entities.Pedidos;
import com.istore.appweb.entities.PedidosItems;
import com.istore.appweb.entities.Productos;
import com.istore.appweb.repositories.PedidosItemsRepository;
import com.istore.appweb.repositories.PedidosRepository;
import com.istore.appweb.repositories.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidosItemsServices {

  @Autowired
  private PedidosItemsRepository repo;

  @Autowired
  private PedidosRepository repoPedidos;

  @Autowired
  private ProductosRepository repoProductos;

  @Autowired
  private PedidosServices pedidosService;

  public PedidosItems getById(Integer id) {
    return repo.findById(id)
        .orElseThrow(() -> new RuntimeException("Item de pedido no encontrado con ID: " + id));
  }

  // Obtiene todos los items de un pedido (para la vista "Detalles")
  public List<PedidosItems> getAllByIdPedido(Integer idPedido) {
    return repo.findByPedidoIdPedido(idPedido);
  }

  @Transactional
  public PedidosItems create(PedidoItemAgregarDTO dto) {
    Pedidos pedido = repoPedidos.findById(dto.getIdPedido())
        .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + dto.getIdPedido()));
    Productos producto = repoProductos.findById(dto.getIdProducto())
        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + dto.getIdProducto()));

    PedidosItems item = new PedidosItems();
    item.setPedido(pedido);
    item.setProducto(producto);
    item.setCantidad(dto.getCantidad());
    item.setPrecio(dto.getPrecio());

    // Lógica de negocio (Calcular Total del Item)
    BigDecimal totalItem = dto.getPrecio().multiply(new BigDecimal(dto.getCantidad()));
    item.setTotal(totalItem);

    PedidosItems itemGuardado = repo.save(item);

    // Lógica de negocio (Recalcular Total del Pedido)
    pedidosService.recalcularTotalPedido(pedido.getIdPedido());

    return itemGuardado;
  }

  @Transactional
  public PedidosItems update(PedidoItemEditarDTO dto) {
    PedidosItems itemExistente = getById(dto.getIdPedidoItem());

    Productos producto = repoProductos.findById(dto.getIdProducto())
        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + dto.getIdProducto()));

    itemExistente.setProducto(producto);
    itemExistente.setCantidad(dto.getCantidad());
    itemExistente.setPrecio(dto.getPrecio());

    // Lógica de negocio (Calcular Total del Item)
    BigDecimal totalItem = dto.getPrecio().multiply(new BigDecimal(dto.getCantidad()));
    itemExistente.setTotal(totalItem);

    PedidosItems itemGuardado = repo.save(itemExistente);

    // Lógica de negocio (Recalcular Total del Pedido)
    pedidosService.recalcularTotalPedido(itemExistente.getPedido().getIdPedido());

    return itemGuardado;
  }

  @Transactional
  public void delete(Integer idPedidoItem) {
    // Busca el item para saber qué pedido recalcular
    PedidosItems itemExistente = getById(idPedidoItem);
    Integer idPedido = itemExistente.getPedido().getIdPedido();

    repo.delete(itemExistente);

    // Lógica de negocio (Recalcular Total del Pedido)
    pedidosService.recalcularTotalPedido(idPedido);
  }
}