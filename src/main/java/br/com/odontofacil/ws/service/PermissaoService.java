package br.com.odontofacil.ws.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.odontofacil.model.Permissao;
import br.com.odontofacil.ws.repository.PermissaoRepository;

@Service
public class PermissaoService {
	
	@Autowired
	PermissaoRepository permissaoRepository;
	
	public List<Permissao> buscarPermissoes(){
		return permissaoRepository.findAll();
	}

}
