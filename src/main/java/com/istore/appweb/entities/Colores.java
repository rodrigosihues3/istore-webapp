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
@Table(name = "colores")
public class Colores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idColor;

    private String nombre;

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    private Boolean eliminado = false;

}
