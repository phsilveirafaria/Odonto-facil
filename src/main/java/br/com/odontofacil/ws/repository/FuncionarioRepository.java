package br.com.odontofacil.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.odontofacil.model.Funcionario;

@Repository
public interface FuncionarioRepository extends JpaRepository <Funcionario, Long>{
	
	@Query("SELECT f FROM Usuario f "
			+ "WHERE f.login = ?1 ")
	public Funcionario findByLogin(String login);
	
	@Query("SELECT COALESCE(MAX(id),0)+1 FROM Usuario u")
	public Long nextId();

	@Query("SELECT f FROM Usuario f "
			+ "WHERE id_permissao = 1")
	public List<Funcionario> buscarDentistas();
}
