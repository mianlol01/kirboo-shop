package com.kirbbo.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kirbbo.app.model.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, String> {
	Optional<Empleado> findByUsernameEmpleadoAndPasswordEmpleado(String usernameEmpleado, String passwordEmpleado);
}
