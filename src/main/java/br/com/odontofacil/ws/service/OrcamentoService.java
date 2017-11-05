package br.com.odontofacil.ws.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.odontofacil.model.Orcamento;
import br.com.odontofacil.ws.repository.OrcamentoRepository;

@Service
public class OrcamentoService {
	
	@Autowired
	OrcamentoRepository orcamentoRepository;
	
	private List <Orcamento> orcamentos = new ArrayList<Orcamento>();

	//Serve para inserir e alterar
	public Orcamento salvar(Orcamento orcamento){
		orcamentoRepository.save(orcamento);
		return orcamento;
	}
	
	public void excluir(Orcamento orcamento){
		orcamentoRepository.delete(orcamento);
	}
	
	public Orcamento buscarPorId(Long id){
		return orcamentoRepository.findOne(id);
		
	}
	
	public 	List<Orcamento> buscarTodos(){
		return orcamentoRepository.findAll();
	}

}
