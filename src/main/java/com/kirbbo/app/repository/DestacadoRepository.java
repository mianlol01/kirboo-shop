package com.kirbbo.app.repository;

import com.kirbbo.app.model.Destacado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestacadoRepository extends JpaRepository<Destacado, Integer> {
	@Query("SELECT d.producto.idProducto FROM Destacado d")
    List<Integer> findAllIds();
}