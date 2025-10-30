package com.istore.appweb.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "tipos_comprobantes")
public class TiposComprobantes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTipoComprobante;

    private String nombre;

    private LocalDateTime fechaCreacion = LocalDateTime.now();

}
