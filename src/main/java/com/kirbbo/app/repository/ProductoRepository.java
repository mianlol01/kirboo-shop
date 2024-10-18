package com.kirbbo.app.repository;

import com.kirbbo.app.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
	List<Producto> findByIdProductoIn(List<Integer> ids);

	List<Producto> findByIdCategoria(int idCategoria);
}