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
@Table(name = "empleado")
public class Empleado {

    @Id
    @Column(name = "id_empleado")
    private String idEmpleado;

    @Column(name = "username_empleado", unique = true, nullable = false)
    private String usernameEmpleado;

    @Column(name = "nombre_empleado", nullable = false)
    private String nombreEmpleado;

    @Column(name = "apellido_empleado", nullable = false)
    private String apellidoEmpleado;

    @Column(name = "password_empleado", nullable = false)
    private String passwordEmpleado;

    @ManyToOne  // Relación muchos a uno con Rol
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;
}
