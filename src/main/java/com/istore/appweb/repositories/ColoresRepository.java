package com.istore.appweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.istore.appweb.entities.Colores;

@Repository
public interface ColoresRepository extends JpaRepository<Colores, Integer>{

}
