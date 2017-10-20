package br.com.odontofacil.ws.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.odontofacil.model.Agendamento;
import br.com.odontofacil.model.Funcionario;

public interface AgendamentoRepository extends JpaRepository <Agendamento, Long>{
	
	@Query("SELECT a FROM Agendamento a "
			+ "INNER JOIN a.funcionario fun "					
			+ "WHERE (DATE(a.start) BETWEEN DATE(?1) AND DATE(?2)) "
			+ "AND fun = ?3)")
	public List<Agendamento> listarPorPeriodoePorProfissional(Calendar dataInicial, Calendar dataFinal, Funcionario funcionario);
	
	@Query("SELECT a FROM Agendamento a "					
			+ "WHERE (DATE(a.start) BETWEEN DATE(?1) AND DATE(?2))")
	public List<Agendamento> listarAllPorPeriodo(Calendar dataInicial, Calendar dataFinal);
	
	@Query("select count(*) from Agendamento")
	public int agendamentosDoMes(Calendar di, Calendar df);

}
