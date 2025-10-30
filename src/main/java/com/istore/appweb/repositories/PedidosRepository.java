package com.istore.appweb.repositories;

import com.istore.appweb.entities.Pedidos;
import com.istore.appweb.entities.Usuarios;
import com.istore.appweb.entities.EstadosCompras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidosRepository extends JpaRepository<Pedidos, Integer> {

  // Historial de pedidos de un usuario específico en fecha descendente
  List<Pedidos> findByUsuarioOrderByFechaCreacionDesc(Usuarios usuario);

  // Para buscar todos los pedidos con un estado
  // (Ej: "Todos los pedidos PENDIENTES")
  List<Pedidos> findByEstadoCompra(EstadosCompras estadoCompra);

  // Para buscar pedidos de un usuario Y que tengan un estado específico
  List<Pedidos> findByUsuarioAndEstadoCompra(Usuarios usuario, EstadosCompras estadoCompra);

}