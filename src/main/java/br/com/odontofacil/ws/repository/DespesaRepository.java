package br.com.odontofacil.ws.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.odontofacil.model.Despesa;
@Repository
public interface DespesaRepository extends JpaRepository <Despesa, Long>{
	
	@Query("SELECT d FROM Despesa d "								
			+ "WHERE (DATE(d.vencimento) BETWEEN DATE(?1) AND DATE(?2)) "		
			+ "ORDER BY d.vencimento ASC")
	public List<Despesa> listarPorPeriodo(Calendar dataInicial, Calendar dataFinal);

}
