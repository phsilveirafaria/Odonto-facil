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
	
	public List<Agendamento> listarPorPeriodo(Calendar dataInicial,Calendar dataFinal,Funcionario funcionario){
		List<Agendamento> agendamentos = agendamentoRepository.listarPorPeriodo(dataInicial, dataFinal, funcionario);
		return agendamentos;
	}

}
