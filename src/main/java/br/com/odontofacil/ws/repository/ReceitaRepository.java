package br.com.odontofacil.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.odontofacil.model.Receita;
@Repository
public interface ReceitaRepository extends JpaRepository <Receita, Long>{

}
