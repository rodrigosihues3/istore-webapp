package com.istore.appweb.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.istore.appweb.entities.EstadosCompras;

@Repository
public interface EstadosComprasRepository extends JpaRepository<EstadosCompras, Integer> {

  boolean existsByNombre(String nombre);

  Optional<EstadosCompras> findByNombre(String nombre);

}