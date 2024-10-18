package com.kirbbo.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
@Entity
@Table(name = "pedido")
public class Pedido {

	@Id
	@Column(name = "id_pedido")
	private String idPedido;

	@ManyToOne
	@JoinColumn(name = "id_cliente", nullable = false)
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name = "id_empleado", nullable = false)
	private Empleado empleado;

	@Column(name = "fecha_pedido", nullable = false)
	private LocalDateTime fechaPedido;

	@Column(name = "fecha_entrega")
	private LocalDateTime fechaEntrega;

	@Column(name = "total", nullable = false)
	private double total;

	@ManyToOne
	@JoinColumn(name = "id_estado", nullable = false)
	private Estado estado;

	@Column(name = "address", nullable = false)
	private String address;

	// Relación con DetallePedido
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DetallePedido> detalles = new ArrayList<>();

	// Método para añadir detalles
	public void addDetalle(DetallePedido detalle) {
		detalles.add(detalle);
		detalle.setPedido(this);
	}

	// Método para eliminar detalles
	public void removeDetalle(DetallePedido detalle) {
		detalles.remove(detalle);
		detalle.setPedido(null);
	}

	public double calcularTotal() {
		double totalPagar = 0;
		for (DetallePedido detalle : detalles) {
			totalPagar += detalle.calcularImporte();
		}
		return totalPagar;
	}

	public int calcularArticulos() {
		return detalles.size();
	}
	public int nada() {
		return 5;
	}
}