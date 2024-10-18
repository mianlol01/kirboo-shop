package com.kirbbo.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Data;

@Data
@Entity
@Table(name = "producto")
public class Producto {

	@Id
	@Column(name = "id_producto")
	private int idProducto;
	@Column(name = "id_categoria")
	private int idCategoria;

	@Column(name = "nombre_producto", nullable = false)
	private String nombreProducto;

	@Column(name = "precio", nullable = false)
	private double precio;

	@Column(name = "stock")
	private int stock;

	@Column(name = "descuento")
	private int descuento;
	
	@ManyToOne
	@JoinColumn(name = "id_categoria", referencedColumnName = "id_categoria", insertable = false, updatable = false)
	private Categoria categoria;

	public String ruta() {
		String r = String.format("p%02d", idProducto);
		return "img/" + r + ".png";
	}

	public double precioFinal() {
		return this.precio - (this.precio * this.descuento / 100);
	}

	public String precioFormato() {
		return String.format("%.2f", this.precio);
	}

	public String precioFinalFormato() {
		return String.format("%.2f", precioFinal());
	}
}
