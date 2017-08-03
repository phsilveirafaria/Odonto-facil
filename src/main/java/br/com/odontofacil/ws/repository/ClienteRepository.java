package br.com.odontofacil.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.odontofacil.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository <Cliente, Long>{

}
