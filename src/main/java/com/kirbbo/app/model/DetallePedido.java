package com.kirbbo.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
@Table(name = "detalle_pedido")
public class DetallePedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_detalle")
	private int idDetalle;

	@ManyToOne
	@JoinColumn(name = "id_pedido", nullable = false)
	@JsonIgnore
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

	public DetallePedido() {
	}

	public String precioUnitarioFormato() {
		return String.format("%.2f", precioUnitario);
	}

	public double calcularImporte() {
	    return cantidad * producto.precioFinal(); // Evita ciclos indirectos
	}

	public String importeFormato() {
		return String.format("%.2f", calcularImporte());
	}

	public DetallePedido(Producto producto) {
		this.descripcion = producto.getNombreProducto();
		this.precioUnitario = producto.getPrecio();
		this.descuento = producto.getDescuento();
		this.producto = producto;
	}
}