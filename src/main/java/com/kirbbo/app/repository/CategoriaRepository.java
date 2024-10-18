package com.kirbbo.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kirbbo.app.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

}
