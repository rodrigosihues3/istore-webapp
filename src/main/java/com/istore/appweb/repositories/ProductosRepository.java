package com.istore.appweb.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.istore.appweb.entities.Productos;

@Repository
public interface ProductosRepository extends JpaRepository<Productos, Integer> {

  Optional<Productos> findBySku(String sku);

  Optional<Productos> findByNombre(String nombre);

  boolean existsBySku(String sku);

  boolean existsByNombre(String nombre);
}
