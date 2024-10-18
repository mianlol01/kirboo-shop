package com.kirbbo.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
@Table(name = "detalle_pedido")
public class DetallePedido {

	@Id
	@Column(name = "id_detalle")
	private int idDetalle;

	@ManyToOne
	@JoinColumn(name = "id_pedido", nullable = false)
	private Pedido pedido;

	@ManyToOne
	@JoinColumn(name = "id_producto", nullable = false)
	private Producto producto;

	@Column(name = "descripcion", length = 255)
	private String descripcion;

	@Column(name = "precio_unitario", nullable = false)
	private double precioUnitario;

	@Column(name = "descuento", nullable = false)
	private int descuento;

	@Column(name = "cantidad", nullable = false)
	private int cantidad;

	@Column(name = "importe", nullable = false)
	private double importe;

	public double calcularImporte() {
		return cantidad * producto.precioFinal();
	}
}