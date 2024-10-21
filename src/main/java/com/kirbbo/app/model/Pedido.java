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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	@JoinColumn(name = "id_empleado")
	private Empleado empleado;

	@Column(name = "fecha_pedido", nullable = true)
	private LocalDateTime fechaPedido;

	@Column(name = "fecha_entrega")
	private LocalDateTime fechaEntrega;

	@Column(name = "total", nullable = false)
	private double total;

	@ManyToOne
	@JoinColumn(name = "id_estado", nullable = true)
	private Estado estado;

	@Column(name = "address", nullable = false)
	private String address;

	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<DetallePedido> detalles = new ArrayList<>();

	public Pedido() {
		this.detalles = new ArrayList<>(); // Inicializa la lista aquí
	}

	public String fechaPedidoFormato() {
		if (fechaPedido == null) {
			return "No disponible"; // O el formato que desees cuando la fecha es nula
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		return fechaPedido.format(formatter);
	}

	public String fechaEntregaFormato() {
		if (fechaEntrega == null) {
			return "No disponible"; // O el formato que desees cuando la fecha es nula
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		return fechaEntrega.format(formatter);
	}

	public void addDetalle(DetallePedido detalle) {
		for (DetallePedido detalleActual : detalles) {
			if (detalleActual.getProducto().getIdProducto() == detalle.getProducto().getIdProducto()) {
				detalleActual.setCantidad(detalleActual.getCantidad() + detalle.getCantidad());
				return;
			}
		}
		detalle.setPedido(this); // Establece la relación inversa
		detalles.add(detalle);
	}

	public int eliminarCarrito(int idProducto) {
		Iterator<DetallePedido> iterator = this.getDetalles().iterator();
		while (iterator.hasNext()) {
			DetallePedido detalle = iterator.next();
			if (detalle.getProducto().getIdProducto() == idProducto) {
				iterator.remove();
				return 1;
			}
		}
		return 0;
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

	public String totalFormato() {
		return String.format("%.2f", calcularTotal());
	}
}