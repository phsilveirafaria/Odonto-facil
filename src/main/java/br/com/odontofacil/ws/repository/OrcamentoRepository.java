package br.com.odontofacil.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.odontofacil.model.Orcamento;
@Repository
public interface OrcamentoRepository extends JpaRepository <Orcamento, Long>{

}
