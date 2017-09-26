package br.com.odontofacil.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.odontofacil.model.Permissao;

@Repository
public interface PermissaoRepository extends JpaRepository <Permissao, Long>{
	
//	@Query("SELECT p FROM Permissao ")
//	public List<Permissao> buscarPermissoes();

}
