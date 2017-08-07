package br.com.odontofacil.ws.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.odontofacil.model.Funcionario;
import br.com.odontofacil.ws.service.FuncionarioService;

@RestController
public class FuncionarioController {

	@Autowired
	FuncionarioService funcionarioService;
	
	
	//Serve para inserir e alterar
		@RequestMapping(method=RequestMethod.POST, value="/salvarFuncionarios", consumes=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Funcionario> salvar(@RequestBody Funcionario funcionario){
			funcionario.setAtivo(true);
			Funcionario funcionarioCadastrado = funcionarioService.salvar(funcionario);
			return new ResponseEntity<>(funcionarioCadastrado, HttpStatus.CREATED);
		}
		
		@RequestMapping(method=RequestMethod.GET, value="/listarFuncionarios", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Collection<Funcionario>> listar() {
			Collection<Funcionario> funcionarios = funcionarioService.buscarTodos();
			return new ResponseEntity<>(funcionarios, HttpStatus.OK);
		}
		
		@RequestMapping(method=RequestMethod.DELETE, value = "/deletarFuncionario/{id}")
		public ResponseEntity<Funcionario> excluirfuncionario(@PathVariable Long id) {
			
			Funcionario funcionarioEncontrado = funcionarioService.buscarPorId(id);
			
			if(funcionarioEncontrado == null){
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			
			funcionarioService.excluir(funcionarioEncontrado);
			
			return new ResponseEntity<>(HttpStatus.OK);
			
		}
		

	}