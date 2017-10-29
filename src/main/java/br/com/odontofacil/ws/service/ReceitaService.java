package br.com.odontofacil.ws.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.odontofacil.model.Receita;
import br.com.odontofacil.ws.repository.ReceitaRepository;

@Service
public class ReceitaService {
	
	@Autowired
	ReceitaRepository receitaRepository;
	
	//Serve para inserir e alterar
	public Receita salvar(Receita receita){
		receitaRepository.save(receita);
		return receita;
	}
	
	public void excluir(Receita receita){
		receitaRepository.delete(receita);
	}
	
	public Receita buscarPorId(Long id){
		return receitaRepository.findOne(id);
		
	}
	
	public 	List<Receita> buscarTodos(){
		return receitaRepository.findAll();
	}


}

