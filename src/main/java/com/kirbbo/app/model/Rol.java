package com.kirbbo.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Data;

@Data
@Entity
@Table(name = "rol")
public class Rol {
	@Id
	@Column(name = "id_rol")
	private int idRol;

	@Column(name = "nombre_rol", nullable = false)
	private String nombreRol;
}
