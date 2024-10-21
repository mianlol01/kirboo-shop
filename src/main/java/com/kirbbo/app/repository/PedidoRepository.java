package com.kirbbo.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kirbbo.app.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, String> {
	@Query("SELECT p.idPedido FROM Pedido p ORDER BY p.idPedido DESC")
	List<String> findAllPedidoIds();

	default Optional<String> findLastPedidoId() {
		List<String> ids = findAllPedidoIds();
		return ids.isEmpty() ? Optional.empty() : Optional.of(ids.get(0));
	}

	List<Pedido> findByCliente_IdCliente(String idCliente);

	List<Pedido> findByEstado_IdEstadoAndEmpleado_IdEmpleado(int idEstado, String idEmpleado);

	List<Pedido> findByEstado_IdEstadoAndCliente_IdCliente(int idEstado, String idCliente);

	// Método para obtener todos los pedidos con idEstado 1
	List<Pedido> findByEstado_IdEstado(int idEstado);

	List<Pedido> findByEmpleadoIsNull();
}
