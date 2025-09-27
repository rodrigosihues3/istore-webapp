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
@Table(name = "usuarios")
public class Usuarios {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer idUsuario;

  private String nombres;

  private String apellidos;

  private String email;

  private String nombreUsuario;

  private String password;

  private String dni;

  private String telefono;

  private String direccion;

  private String rol;

  private LocalDateTime fechaCreacion = LocalDateTime.now();

  private Boolean eliminado = false;

}
