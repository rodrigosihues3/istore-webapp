package com.istore.appweb.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.istore.appweb.entities.EstadosPagos;

@Repository
public interface EstadosPagosRepository extends JpaRepository<EstadosPagos, Integer> {

  boolean existsByNombre(String nombre);

  Optional<EstadosPagos> findByNombre(String nombre);

}