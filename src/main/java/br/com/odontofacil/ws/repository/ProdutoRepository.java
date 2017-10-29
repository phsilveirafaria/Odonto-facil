package br.com.odontofacil.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.odontofacil.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
