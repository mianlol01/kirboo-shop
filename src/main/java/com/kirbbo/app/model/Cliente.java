package com.kirbbo.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Data;

@Data
@Entity
@Table(name = "cliente")
public class Cliente {
	@Id
	@Column(name = "id_cliente", length = 5)
	private String idCliente;

	@Column(name = "username_cliente", length = 50, nullable = false, unique = true)

	private String usernameCliente;

	@Column(name = "nombre_cliente", length = 50, nullable = false)
	private String nombreCliente;

	@Column(name = "apellido_cliente", length = 50, nullable = false)
	private String apellidoCliente;

	@Column(name = "address_cliente", length = 255, nullable = false)
	private String addressCliente;

	@Column(name = "password_cliente", length = 50, nullable = false)
	private String passwordCliente;
}
