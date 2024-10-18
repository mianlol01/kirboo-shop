package com.kirbbo.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kirbbo.app.model.Categoria;
import com.kirbbo.app.model.Producto;
import com.kirbbo.app.service.CategoriaService;
import com.kirbbo.app.service.ProductoService;

@Controller
public class ShopController {

	@Autowired
	private ProductoService productoService;
	@Autowired
	private CategoriaService categoriaService;

	@GetMapping({ "/", "/home", "/inicio", "/index" })
	public String home(Model model) {
		List<Producto> listaDestacados = productoService.obtenerProductosDestacados();
		model.addAttribute("listaDestacados", listaDestacados);
		return "index";
	}

	@GetMapping({ "/shop", "/menu", "/store" })
	public String menu(@RequestParam(required = false) Integer categoria, Model model) {
		List<Producto> listaProductos = new ArrayList<Producto>();
		if (categoria != null) {
			Categoria c = categoriaService.obtenerCategoria(categoria);
			model.addAttribute("nombreCategoria", c.getNombreCategoria().toUpperCase());
			listaProductos = productoService.obtenerProductosPorCategoria(categoria);
		} else {
			model.addAttribute("nombreCategoria", "TODOS");
			listaProductos = productoService.obtenerTodosLosProductos();
		}
		List<Categoria> listaCategorias = categoriaService.obtenerCategorias();
		model.addAttribute("listaProductos", listaProductos);
		model.addAttribute("listaCategorias", listaCategorias);

		return "menu";
	}

	@GetMapping({ "/detalles" })
	public String producto(@RequestParam(required = true) Integer producto, Model model) {
		Producto p = productoService.obtenerProductoPorId(producto);
		List<Producto> listaSimilares = productoService.obtenerProductosPorCategoria(p.getIdCategoria());
		model.addAttribute("p", p);
		model.addAttribute("listaSimilares", listaSimilares);
		return "producto";
	}

	@GetMapping({ "/ordenar", "/carrito", "/cart" })
	public String ordenar(Model model) {
		List<Producto> listaDestacados = productoService.obtenerProductosDestacados();
		model.addAttribute("listaDestacados", listaDestacados);
		return "ordenar";
	}
}