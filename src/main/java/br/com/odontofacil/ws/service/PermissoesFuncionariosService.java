package br.com.odontofacil.ws.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.odontofacil.model.PermissoesFuncionarios;
import br.com.odontofacil.ws.repository.PermissoesFuncionarioRepository;

@Service
public class PermissoesFuncionariosService {
	
	@Autowired
	PermissoesFuncionarioRepository permissoesFuncionarioRepository;
	
	public PermissoesFuncionarios save(PermissoesFuncionarios permissoesFuncionarios){
		return permissoesFuncionarioRepository.save(permissoesFuncionarios);
	}

}
