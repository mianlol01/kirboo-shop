package com.kirbbo.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kirbbo.app.model.Empleado;
import com.kirbbo.app.repository.EmpleadoRepository;

@Service
public class EmpleadoService {
	@Autowired
	EmpleadoRepository empleadoRepository;
	
	public Empleado autenticarEmpleado(String username, String password) {
		// Busca al cliente por username y password
		return empleadoRepository.findByUsernameEmpleadoAndPasswordEmpleado(username, password)
				.orElseThrow(() -> new RuntimeException("Credenciales inválidas"));
	}
}
