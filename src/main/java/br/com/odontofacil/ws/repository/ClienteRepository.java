package br.com.odontofacil.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.odontofacil.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository <Cliente, Long>{

			@Query("SELECT u FROM Usuario u WHERE "
					+ "MONTH(data_inclusao) = MONTH(now()) and dtype = 'Cliente'")
			public List<Cliente> listarNovosUsuarios();
	
}
