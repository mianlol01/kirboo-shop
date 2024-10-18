package com.kirbbo.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Data;

@Data
@Entity
@Table(name = "categoria")
public class Categoria {
	@Id
	@Column(name = "id_categoria")
	private int idCategoria;

	@Column(name = "nombre_categoria", length = 50, nullable = false)
	private String nombreCategoria;
}
