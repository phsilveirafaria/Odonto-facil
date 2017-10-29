package br.com.odontofacil.ws.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.odontofacil.model.Despesa;
import br.com.odontofacil.ws.repository.DespesaRepository;

@Service
public class DespesaService {
	
	@Autowired
	DespesaRepository despesaRepository;
	
	//Serve para inserir e alterar
	public Despesa salvar(Despesa despesa){
		despesaRepository.save(despesa);
		return despesa;
	}
	
	public void excluir(Despesa despesa){
		despesaRepository.delete(despesa);
	}
	
	public Despesa buscarPorId(Long id){
		return despesaRepository.findOne(id);
		
	}
	
	public 	List<Despesa> buscarTodos(){
		return despesaRepository.findAll();
	}
	
	public 	List<Despesa> listarPorPeriodo(Calendar dataInicial, Calendar dataFinal){
		return despesaRepository.listarPorPeriodo(dataInicial, dataFinal);
	}


}
