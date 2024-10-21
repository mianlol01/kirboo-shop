package com.kirbbo.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kirbbo.app.model.DetallePedido;
import com.kirbbo.app.model.Empleado;
import com.kirbbo.app.model.Estado;
import com.kirbbo.app.model.Pedido;
import com.kirbbo.app.model.Producto;
import com.kirbbo.app.repository.DetallePedidoRepository;
import com.kirbbo.app.repository.PedidoRepository;
import com.kirbbo.app.repository.ProductoRepository;

@Service
public class PedidoService {
	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private ProductoRepository productoRepository;
	@Autowired
	private DetallePedidoRepository detallePedidoRepository;

	private String generarNuevoIdPedido() {
		System.out.println("-1");
		Optional<String> lastIdOpt = pedidoRepository.findLastPedidoId();
		System.out.println("-2");
		if (!lastIdOpt.isPresent()) {
			return "P0001";
		}
		System.out.println("-3");
		String lastId = lastIdOpt.get();
		System.out.println("-4");
		int idNum = Integer.parseInt(lastId.substring(1));
		System.out.println("-5");
		return String.format("P%04d", idNum + 1);
	}

	public int generarPedido(Pedido p) {
		try {
			System.out.println("1");
			p.setIdPedido(generarNuevoIdPedido());
			System.out.println("2");
			p.setTotal(p.calcularTotal());
			System.out.println("3");

			// Calcula el importe de cada detalle y asocia el pedido
			for (DetallePedido detalle : p.getDetalles()) {
				detalle.setImporte(detalle.calcularImporte());
				detalle.setPedido(p); // Establece la relación con el pedido
			}
			System.out.println("4");

			// Establece el estado del pedido
			Estado estado = new Estado();
			estado.setIdEstado(1);
			p.setEstado(estado);

			// Establece la fecha del pedido
			p.setFechaPedido(LocalDateTime.now());
			System.out.println("5");

			List<DetallePedido> detalles = p.getDetalles();
			p.setDetalles(null); // Asegúrate de que la lista de detalles esté en null
			pedidoRepository.save(p); // Guarda el pedido
			System.out.println("6");

			// Guarda los detalles del pedido por separado
			for (DetallePedido detalle : detalles) {
				detalle.setPedido(p); // Asegúrate de que el detalle tenga el pedido
				detallePedidoRepository.save(detalle); // Guarda cada detalle
			}
			// actualizar stock
			for (DetallePedido detallePedido : detalles) {
				Producto c = detallePedido.getProducto();
				c.setStock(c.getStock() - detallePedido.getCantidad());
				productoRepository.save(c);
			}
			return 1;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return 0; // Retorna 0 para indicar un error
		}
	}

	public List<Pedido> obtenerPedidosPorCliente(String idCliente) {
		return pedidoRepository.findByCliente_IdCliente(idCliente);
	}

	// Obtener pedidos con idEstado 1
	public List<Pedido> obtenerPedidosConEstado1(String idCliente) {
		return pedidoRepository.findByEstado_IdEstadoAndCliente_IdCliente(1, idCliente);
	}

	// Obtener pedidos con idEstado 2
	public List<Pedido> obtenerPedidosConEstado2(String idCliente) {
		return pedidoRepository.findByEstado_IdEstadoAndCliente_IdCliente(2, idCliente);
	}

	// Obtener todos los pedidos con idEstado 1
	public List<Pedido> obtenerPedidosConEstado1() {
		return pedidoRepository.findByEstado_IdEstado(1);
	}

	public int asignarPedido(String idPedido, Empleado empleado) {
		try {
			Optional<Pedido> p = pedidoRepository.findById(idPedido);
			Pedido pedido = p.get();
			pedido.setEmpleado(empleado);
			pedidoRepository.save(pedido);
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}

	public List<Pedido> obtenerPedidosPorEmpleado(String idEmpleado) {
		return pedidoRepository.findByEstado_IdEstadoAndEmpleado_IdEmpleado(1, idEmpleado);
	}

	public List<Pedido> obtenerPedidosSinAsignar() {
		return pedidoRepository.findByEmpleadoIsNull();
	}

	public int finalizarPedido(String idPedido) {
		try {
			Optional<Pedido> p = pedidoRepository.findById(idPedido);
			Pedido pedido = p.get();
			Estado e = new Estado();
			e.setIdEstado(2);
			pedido.setEstado(e);
			pedido.setFechaEntrega(LocalDateTime.now());;
			pedidoRepository.save(pedido);
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}
}
