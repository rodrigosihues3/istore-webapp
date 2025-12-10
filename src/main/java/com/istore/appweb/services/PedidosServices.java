package com.istore.appweb.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.istore.appweb.DTO.pedidos.PedidoAgregarDTO;
import com.istore.appweb.DTO.pedidos.PedidoEditarDTO;
import com.istore.appweb.entities.EstadosCompras;
import com.istore.appweb.entities.EstadosPagos;
import com.istore.appweb.entities.MetodosPagos;
import com.istore.appweb.entities.Pedidos;
import com.istore.appweb.entities.TiposComprobantes;
import com.istore.appweb.entities.Usuarios;
import com.istore.appweb.repositories.TiposComprobantesRepository;
import com.istore.appweb.repositories.UsuariosRepository;

import jakarta.transaction.Transactional;

import com.istore.appweb.repositories.EstadosComprasRepository;
import com.istore.appweb.repositories.EstadosPagosRepository;
import com.istore.appweb.repositories.MetodosPagosRepository;
import com.istore.appweb.repositories.PedidosItemsRepository;
import com.istore.appweb.repositories.PedidosRepository;

@Service
public class PedidosServices {

  @Autowired
  private PedidosRepository repo;

  @Autowired
  private UsuariosRepository repoUsuarios;

  @Autowired
  private MetodosPagosRepository repoMetodosPago;

  @Autowired
  private TiposComprobantesRepository repoTiposComprobante;

  @Autowired
  private EstadosComprasRepository repoEstadosCompras;

  @Autowired
  private EstadosPagosRepository repoEstadosPagos;

  @Autowired
  private PedidosItemsRepository repoPedidosItems;

  public List<Pedidos> getAll() {
    return repo.findAll(Sort.by(Sort.Direction.DESC, "fechaActualizacion"));
  }

  public Pedidos getById(Integer id) {
    return repo.findById(id).orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
  }

  public void deleteById(Integer id) {
    repo.deleteById(id);
  }

  public Pedidos create(PedidoAgregarDTO pedidoDTO) {
    // Buscar las entidades foráneas
    Usuarios usuario = repoUsuarios.findById(pedidoDTO.getIdUsuario())
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + pedidoDTO.getIdUsuario()));

    MetodosPagos metodoPago = repoMetodosPago.findById(pedidoDTO.getIdMetodoPago())
        .orElseThrow(() -> new RuntimeException("Método de Pago no encontrado con ID: " + pedidoDTO.getIdMetodoPago()));

    TiposComprobantes tipoComprobante = repoTiposComprobante.findById(pedidoDTO.getIdTipoComprobante())
        .orElseThrow(() -> new RuntimeException(
            "Tipo de Comprobante no encontrado con ID: " + pedidoDTO.getIdTipoComprobante()));

    EstadosCompras estadoCompra = repoEstadosCompras.findById(pedidoDTO.getIdEstadoCompra())
        .orElseThrow(() -> new RuntimeException(
            "Estado de Compra no encontrado con ID: " + pedidoDTO.getIdEstadoCompra()));

    EstadosPagos estadoPago = repoEstadosPagos.findById(pedidoDTO.getIdEstadoPago())
        .orElseThrow(() -> new RuntimeException("Estado de Pago no encontrado"));

    // Objeto de Pedidos a agregar en la BD
    Pedidos pedido = new Pedidos();

    pedido.setUsuario(usuario);
    pedido.setMetodoPago(metodoPago);
    pedido.setTipoComprobante(tipoComprobante);
    pedido.setEstadoCompra(estadoCompra);
    pedido.setEstadoPago(estadoPago);
    pedido.setReferenciaPago(pedidoDTO.getReferenciaPago());

    pedido.setFechaActualizacion(pedido.getFechaCreacion());
    // Lógica de negocio
    // El 'total' temporal es 0 porque aún no esta la tabla carritos.
    pedido.setTotal(BigDecimal.ZERO);

    return repo.save(pedido);
  }

  public Pedidos update(PedidoEditarDTO pedidoDTO) {
    Pedidos pedidoExistente = getById(pedidoDTO.getIdPedido());

    // Buscar las nuevas entidades foráneas
    MetodosPagos metodoPago = repoMetodosPago.findById(pedidoDTO.getIdMetodoPago())
        .orElseThrow(() -> new RuntimeException("Método de Pago no encontrado con ID: " + pedidoDTO.getIdMetodoPago()));

    TiposComprobantes tipoComprobante = repoTiposComprobante.findById(pedidoDTO.getIdTipoComprobante())
        .orElseThrow(() -> new RuntimeException(
            "Tipo de Comprobante no encontrado con ID: " + pedidoDTO.getIdTipoComprobante()));

    EstadosCompras estadoCompra = repoEstadosCompras.findById(pedidoDTO.getIdEstadoCompra())
        .orElseThrow(() -> new RuntimeException(
            "Estado de Compra no encontrado con ID: " + pedidoDTO.getIdEstadoCompra()));

    EstadosPagos estadoPago = repoEstadosPagos.findById(pedidoDTO.getIdEstadoPago())
        .orElseThrow(() -> new RuntimeException("Estado de Pago no encontrado"));

    pedidoExistente.setMetodoPago(metodoPago);
    pedidoExistente.setTipoComprobante(tipoComprobante);
    pedidoExistente.setEstadoCompra(estadoCompra);
    pedidoExistente.setEstadoPago(estadoPago);
    pedidoExistente.setReferenciaPago(pedidoDTO.getReferenciaPago());

    // Asignar nueva lógica de negocio
    // (El 'idUsuario' NO se puede cambiar)
    // (El 'total' se mantiene sin editar)
    // Solo se actualiza "fechaAtualizacion", ya que corresponde a un historial,
    // evaluar gregar una nueva tabla para detalles de cambios mas adelante
    pedidoExistente.setFechaActualizacion(LocalDateTime.now());

    return repo.save(pedidoExistente);
  }

  // Recalcula el 'total' de un Pedido basándose en sus items.
  @Transactional // Asegura que la operación de lectura y guardado sea atómica
  public void recalcularTotalPedido(Integer idPedido) {
    Pedidos pedido = getById(idPedido);
    BigDecimal nuevoTotal = repoPedidosItems.sumTotalByPedidoId(idPedido);
    pedido.setTotal(nuevoTotal == null ? BigDecimal.ZERO : nuevoTotal);
    repo.save(pedido);
  }

}