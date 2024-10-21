package com.kirbbo.app.service;

import com.kirbbo.app.model.Producto;
import com.kirbbo.app.repository.DestacadoRepository; // Asegúrate de tener este repositorio
import com.kirbbo.app.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private DestacadoRepository destacadoRepository;

	public List<Producto> obtenerTodosLosProductos() {
		return productoRepository.findAll();
	}

	public List<Producto> obtenerProductosDestacados() {
		List<Integer> idsDestacados = destacadoRepository.findAllIds();
		return productoRepository.findByIdProductoIn(idsDestacados);
	}

	public List<Producto> obtenerProductosPorCategoria(int idCategoria) {
		return productoRepository.findByIdCategoria(idCategoria);
	}

	public Producto obtenerProductoPorId(int idProducto) {
		return productoRepository.findById(idProducto).get();
	}

	public int actualizar(Producto p) {
		try {
			Producto producto = obtenerProductoPorId(p.getIdProducto());
			producto.setDescuento(p.getDescuento());
			producto.setPrecio(p.getPrecio());
			producto.setStock(p.getStock());
			productoRepository.save(producto);
			return 1;
		} catch (Exception e) {
			return 0;
		}

	}
}
