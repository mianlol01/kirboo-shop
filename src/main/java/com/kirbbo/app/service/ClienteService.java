package com.kirbbo.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kirbbo.app.model.Cliente;
import com.kirbbo.app.repository.ClienteRepository;

import jakarta.transaction.Transactional;

@Service
public class ClienteService {
	@Autowired
	private ClienteRepository clienteRepository;

	public boolean isUsernameAvailable(String username) {
		return clienteRepository.findByUsernameCliente(username).isEmpty();
	}

	private String generarNuevoIdCliente() {
		// Obtener el último ID de cliente (si existe)
		Optional<String> lastIdOpt = clienteRepository.findLastClienteId();

		// Si no existe un cliente previo, se comienza con "C0001"
		if (!lastIdOpt.isPresent()) {
			return "C0001";
		}

		String lastId = lastIdOpt.get(); // Ejemplo: "C0009"
		int idNum = Integer.parseInt(lastId.substring(1)); // Extrae el número: 9

		// Generar el siguiente ID sumando 1 y rellenando con ceros
		return String.format("C%04d", idNum + 1); // Ejemplo: "C0010"
	}

	public int registrarCliente(Cliente cliente) {
		if (!isUsernameAvailable(cliente.getUsernameCliente())) {
			return 0;
		}
		cliente.setIdCliente(generarNuevoIdCliente());
		clienteRepository.save(cliente);
		return 1;
	}

	public Cliente autenticarCliente(String username, String password) {
		// Busca al cliente por username y password
		return clienteRepository.findByUsernameClienteAndPasswordCliente(username, password)
				.orElseThrow(() -> new RuntimeException("Credenciales inválidas"));
	}

	@Transactional
	public void actualizarDireccion(String idCliente, String nuevaDireccion) {
		clienteRepository.updateAddressCliente(idCliente, nuevaDireccion);
	}

	public Cliente obtenerClientePorId(String idCliente) {
		return clienteRepository.findById(idCliente).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
	}

	@Transactional
	public void actualizarTelefono(String idCliente, String nuevoTelefono) {
		clienteRepository.updateTelefonoCliente(idCliente, nuevoTelefono);
	}
}
