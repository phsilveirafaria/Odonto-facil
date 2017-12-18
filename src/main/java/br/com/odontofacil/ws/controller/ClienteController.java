package br.com.odontofacil.ws.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.odontofacil.model.Cliente;
import br.com.odontofacil.model.Funcionario;
import br.com.odontofacil.util.Util;
import br.com.odontofacil.ws.service.ClienteService;

@RestController
public class ClienteController {

	@Autowired
	ClienteService clienteService;
	
	
	//Serve para inserir e alterar
		@RequestMapping(method=RequestMethod.POST, value="/salvarClientes", consumes=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Cliente> salvar(@RequestBody Cliente cliente) throws Exception{
			
			Cliente clienteExiste = this.clienteService.buscarPorId(cliente.getIdUsuario());
			
				if (cliente != null) {
					if (cliente.getCpf_cnpj() != null && !cliente.getCpf_cnpj().trim().isEmpty()) {
						try {
							Util.validarCPF(cliente.getCpf_cnpj());
						} catch (Exception ex) {
							System.out.println("CPF " + cliente.getCpf_cnpj() + " do cliente é inválido");
							//comentado para defesa do tcc para que caso não tenha conexão com a internet, não quebre o sistema.
							//	SalvarEnviarLogs.gravarArquivo(ex);
							throw new Exception("O CPF é inválido!");
						}
					}
				}
			cliente.setAtivo(true);
			if(clienteExiste == null){
				cliente.setDataInclusao(Calendar.getInstance());
			}
			Cliente clienteCadastrado = clienteService.salvar(cliente);
			return new ResponseEntity<>(clienteCadastrado, HttpStatus.CREATED);
		}
		
		
		@RequestMapping(method=RequestMethod.GET, value="/listarClientes", produces=MediaType.APPLICATION_JSON_VALUE)
		public List<Cliente> listar() {
			List<Cliente> clientes = clienteService.buscarTodos();
			List<Cliente> clientesAtivos = new ArrayList<Cliente>();
			for(Cliente cliente: clientes){
				if(cliente.getAtivo().equals(true)){
					clientesAtivos.add(cliente);
				}
			}
			return (clientesAtivos);
		}
		
		@RequestMapping(method=RequestMethod.POST, value = "/excluirClientes")
		public ResponseEntity<Cliente> excluirCliente(@RequestBody Cliente cliente) {
			
			Cliente clienteEncontrado = clienteService.buscarPorId(cliente.getIdUsuario());
			
			if(clienteEncontrado == null){
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			
			clienteService.excluir(clienteEncontrado);
			
			return new ResponseEntity<>(HttpStatus.OK);
			
		}
		
		@RequestMapping(method=RequestMethod.GET, value="/listarNovosUsuarios", produces=MediaType.APPLICATION_JSON_VALUE)
		public int listarNovosUsuarios() {
			List<Cliente> clientes = clienteService.listarNovosUsuarios();
			return (clientes.size());
		}

	}