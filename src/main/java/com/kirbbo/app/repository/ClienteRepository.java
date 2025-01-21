package com.kirbbo.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kirbbo.app.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, String> {

	Optional<Cliente> findByUsernameCliente(String usernameCliente);

	@Query("SELECT c.idCliente FROM Cliente c ORDER BY c.idCliente DESC")
	List<String> findAllClienteIds();

	default Optional<String> findLastClienteId() {
		List<String> ids = findAllClienteIds();
		return ids.isEmpty() ? Optional.empty() : Optional.of(ids.get(0));
	}

	Optional<Cliente> findByUsernameClienteAndPasswordCliente(String usernameCliente, String passwordCliente);

	@Modifying
	@Query("UPDATE Cliente c SET c.addressCliente = :address WHERE c.idCliente = :id")
	void updateAddressCliente(@Param("id") String id, @Param("address") String address);

	@Modifying
	@Query("UPDATE Cliente c SET c.telefonoCliente = :telefono WHERE c.idCliente = :id")
	void updateTelefonoCliente(@Param("id") String id, @Param("telefono") String telefono);
}
