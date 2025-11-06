package com.istore.appweb.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pedidos_items")
public class PedidosItems {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer idPedidoItem;

  private Integer cantidad;

  @Column(precision = 12, scale = 2)
  private BigDecimal precio;

  @Column(precision = 14, scale = 2)
  private BigDecimal total;

  @ManyToOne
  @JoinColumn(name = "id_pedido")
  private Pedidos pedido;

  @ManyToOne
  @JoinColumn(name = "id_producto")
  private Productos producto;

  private LocalDateTime fechaCreacion = LocalDateTime.now();

}
