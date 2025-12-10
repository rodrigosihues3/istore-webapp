package com.istore.appweb.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@Table(name = "productos")
public class Productos {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer idProducto;

  private String sku;
  private String nombre;
  private String descripcion;
  private BigDecimal precio;
  private Integer stock;
  private String urlImagen;

  @ManyToOne
  @JoinColumn(name = "id_categoria")
  private Categorias categoria;

  @ManyToOne
  @JoinColumn(name = "id_color")
  private Colores color;

  private LocalDateTime fechaCreacion = LocalDateTime.now();
}
