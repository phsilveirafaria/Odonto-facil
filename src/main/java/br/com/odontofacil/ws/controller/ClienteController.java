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

import br.com.odontofacil.model.Cliente;
import br.com.odontofacil.ws.service.ClienteService;

@RestController
public class ClienteController {

	@Autowired
	ClienteService clienteService;
	
	
	//Serve para inserir e alterar
		@RequestMapping(method=RequestMethod.POST, value="/salvarClientes", consumes=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Cliente> salvar(@RequestBody Cliente cliente){
			cliente.setAtivo(true);
			Cliente clienteCadastrado = clienteService.salvar(cliente);
			return new ResponseEntity<>(clienteCadastrado, HttpStatus.CREATED);
		}
		
		@RequestMapping(method=RequestMethod.GET, value="/listarClientes", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Collection<Cliente>> listar() {
			Collection<Cliente> clientes = clienteService.buscarTodos();
			return new ResponseEntity<>(clientes, HttpStatus.OK);
		}
		
		@RequestMapping(method=RequestMethod.DELETE, value = "/deletarCliente/{id}")
		public ResponseEntity<Cliente> excluirCliente(@PathVariable Long id) {
			
			Cliente clienteEncontrado = clienteService.buscarPorId(id);
			
			if(clienteEncontrado == null){
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			
			clienteService.excluir(clienteEncontrado);
			
			return new ResponseEntity<>(HttpStatus.OK);
			
		}
		

	}