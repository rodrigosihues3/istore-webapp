package com.istore.appweb.repositories;

import com.istore.appweb.entities.Pedidos;
import com.istore.appweb.entities.PedidosItems;
import com.istore.appweb.entities.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PedidosItemsRepository extends JpaRepository<PedidosItems, Integer> {

  // Para obtener todos los items que pertenecen a un solo pedido
  List<PedidosItems> findByPedido(Pedidos pedido);

  // Para ver todas las veces que un producto ha sido vendido
  List<PedidosItems> findByProducto(Productos producto);

  // MÉTODO DE VALIDACIÓN:
  // Para ver si un producto específico ya existe dentro de un pedido
  boolean existsByPedidoAndProducto(Pedidos pedido, Productos producto);

  // Suma todos los 'total' de un pedido específico.
  @Query("SELECT SUM(pi.total) FROM PedidosItems pi WHERE pi.pedido.idPedido = :idPedido")
  BigDecimal sumTotalByPedidoId(@Param("idPedido") Integer idPedido);

  // Para la vista de "Detalles" (trae todos los items de 1 pedido)
  List<PedidosItems> findByPedidoIdPedido(Integer idPedido);

}