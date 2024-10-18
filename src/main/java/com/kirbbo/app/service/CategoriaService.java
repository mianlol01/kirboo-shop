package com.kirbbo.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kirbbo.app.model.Categoria;
import com.kirbbo.app.repository.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;

	public List<Categoria> obtenerCategorias() {
		return categoriaRepository.findAll();
	}
	public Categoria obtenerCategoria(int idCategoria) {
		return categoriaRepository.findById(idCategoria).get();
	}
}
