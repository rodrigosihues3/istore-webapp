package com.istore.appweb.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "pedidos")
public class Pedidos {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer idPedido;

  @Column(precision = 12, scale = 2)
  private BigDecimal total;

  @ManyToOne
  @JoinColumn(name = "id_usuario")
  private Usuarios usuario;

  @ManyToOne
  @JoinColumn(name = "id_tipo_comprobante")
  private TiposComprobantes tipoComprobante;

  @ManyToOne
  @JoinColumn(name = "id_metodo_pago")
  private MetodosPagos metodoPago;

  @ManyToOne
  @JoinColumn(name = "id_estado_compra")
  private EstadosCompras estadoCompra;

  @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
  private List<PedidosItems> items;

  private LocalDateTime fechaCreacion = LocalDateTime.now();
  private LocalDateTime fechaActualizacion;

}
