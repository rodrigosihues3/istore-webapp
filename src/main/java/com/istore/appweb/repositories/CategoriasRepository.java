package com.istore.appweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.istore.appweb.entities.Categorias;


@Repository
public interface CategoriasRepository extends JpaRepository<Categorias, Integer>{

}
