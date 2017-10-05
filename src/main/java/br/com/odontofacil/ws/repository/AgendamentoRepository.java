package br.com.odontofacil.ws.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.odontofacil.model.Agendamento;
import br.com.odontofacil.model.Funcionario;

public interface AgendamentoRepository extends JpaRepository <Agendamento, Long>{
	
//	@Query("SELECT a FROM Agendamento a "
//			+ "INNER JOIN a.cliente p "
//			+ "INNER JOIN f.funcionario fun "					
//			+ "WHERE ((DATE(a.start) BETWEEN DATE(?1) AND DATE(?2)) "
//			+ "OR (DATE(a.start) <= DATE(?1)) "
//			+ "AND fun = ?3")
	@Query("Select a from Agendamento a")
	public List<Agendamento> listarPorPeriodo(Calendar dataInicial, Calendar dataFinal, Funcionario funcionario);

}
