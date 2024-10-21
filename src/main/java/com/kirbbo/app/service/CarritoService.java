package com.kirbbo.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kirbbo.app.model.DetallePedido;
import com.kirbbo.app.model.Pedido;
import com.kirbbo.app.model.Producto;

@Service
public class CarritoService {

	@Autowired
	private ProductoService productoService;

	public DetallePedido crearDetalle(int idProducto, int cantidad) {
		Producto p = productoService.obtenerProductoPorId(idProducto);
		DetallePedido dp = new DetallePedido(p);
		dp.setCantidad(cantidad);
		return dp;
	}

	public int[] agregarCarrito(Pedido carrito, int idProducto, int cantidad) {
		DetallePedido dp = crearDetalle(idProducto, cantidad);
		for (DetallePedido detalleActual : carrito.getDetalles()) {
			if (detalleActual.getProducto().getIdProducto() == dp.getProducto().getIdProducto()) {
				if (detalleActual.getCantidad() + dp.getCantidad() > dp.getProducto().getStock()) {
					System.out.println("Cantidad excedida");
					int[] resultado = { -1, detalleActual.getCantidad() };
					return resultado;
				}
				carrito.addDetalle(dp);
				int[] resultado = { 1, dp.getCantidad() };
				return resultado;
			}
		}
		carrito.addDetalle(dp);
		int[] resultado = { 1, dp.getCantidad() };
		return resultado;
	}
}