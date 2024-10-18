package com.kirbbo.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Data;

@Data
@Entity
@Table(name = "estado")
public class Estado {
	@Id
	@Column(name = "id_estado")
	private int idEstado;

	@Column(name = "nombre_estado", length = 50, nullable = false)
	private String nombreEstado;
}
