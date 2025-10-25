package com.istore.appweb.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "carrito")

public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCarrito;


    private String sesionToken;


    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    //llave foranea, trae toda la entidad
    @OneToOne// uno a uno
    @JoinColumn //busca el id en la tabla
    private Usuarios usuario;
//siuuuuuu
    @OneToOne
    @JoinColumn
    private EstadosCompras estadoCompra;
}
