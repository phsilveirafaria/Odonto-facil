package br.com.odontofacil.ws.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.odontofacil.model.Agendamento;
import br.com.odontofacil.model.Funcionario;
import br.com.odontofacil.ws.repository.AgendamentoRepository;

@Service
public class AgendamentoService {
	
	@Autowired
	AgendamentoRepository agendamentoRepository;
	
	//Serve para inserir e alterar
		public Agendamento salvar(Agendamento agendamento){
			agendamentoRepository.save(agendamento);
			return agendamento;
		}
		
		public void excluir(Agendamento agendamento){
			agendamentoRepository.delete(agendamento);
		}
	
	public List<Agendamento> listarPorPeriodoePorProfissional(Calendar dataInicial,Calendar dataFinal,Funcionario funcionario){
		List<Agendamento> agendamentos = agendamentoRepository.listarPorPeriodoePorProfissional(dataInicial, dataFinal, funcionario);
		return agendamentos;
	}
	
	public int agendamentosDoMes(Calendar dataInicial,Calendar dataFinal){
		int agendamentosDoMes = agendamentoRepository.agendamentosDoMes(dataInicial, dataFinal);
		return agendamentosDoMes;
	}
	
	public List<Agendamento> listarAllPorPeriodo(Calendar dataInicial,Calendar dataFinal){
		List<Agendamento> agendamentos = agendamentoRepository.listarAllPorPeriodo(dataInicial, dataFinal);
		return agendamentos;
	}

}
