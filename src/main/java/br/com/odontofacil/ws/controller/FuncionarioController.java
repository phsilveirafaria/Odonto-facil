package br.com.odontofacil.ws.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.odontofacil.model.Agendamento;
import br.com.odontofacil.model.Funcionario;
import br.com.odontofacil.pojo.CredenciaisFuncionario;
import br.com.odontofacil.util.Util;
import br.com.odontofacil.ws.service.AgendamentoService;
import br.com.odontofacil.ws.service.FuncionarioService;

@RestController
public class FuncionarioController {

	@Autowired
	FuncionarioService funcionarioService;
	
	@Autowired
	AgendamentoService agendamentoService;
	
private final static Logger logger = Logger.getLogger(FuncionarioController.class);
	
	private static void logMessage(String msg, boolean error) {
    	if(!error && logger.isDebugEnabled()){
    	    logger.debug(msg);
    	}

    	//logs an error message with parameter
    	if (error) {
    		logger.error(msg);
    	}
    }
	
	
	//Serve para inserir e alterar
		@RequestMapping(method=RequestMethod.POST, value="/salvarFuncionarios", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
		public Funcionario salvar(@RequestBody CredenciaisFuncionario cf) throws Exception{
			
			Funcionario funcionario = cf.getFuncionario();
			funcionario.setSenha((new BCryptPasswordEncoder().encode(cf.getSenha())).getBytes());
			
			//if()
			Funcionario funcionarioExiste = this.funcionarioService.buscaPorLogin(funcionario.getLogin());
			if (funcionarioExiste != null) {
				throw new Exception("Usuário já cadastrado!");
			}
			
			if (funcionario.getCpf_cnpj() != null && !funcionario.getCpf_cnpj().trim().isEmpty()) {
				try {
					Util.validarCPF(funcionario.getCpf_cnpj());
				} catch(Exception ex) {
					logMessage("CPF " + funcionario.getCpf_cnpj() + " do psicólogo é inválido", true);
					throw new Exception("O CPF é inválido!");
				}
			}
			
			funcionario.setChave(Util.gerarChave().getBytes());		
			funcionario.setIdUsuario(this.funcionarioService.proximoId());	
			
						
			Funcionario funcionarioCadastrado = funcionarioService.salvar(funcionario);
			
			return funcionarioCadastrado;
		}
		
		
		@RequestMapping(method=RequestMethod.GET, value="/listarValoresPorFuncionario", produces=MediaType.APPLICATION_JSON_VALUE)
		public BigDecimal listarValoresPorFuncionario(Principal f) throws Exception{
			
			Funcionario funcionario = this.funcionarioService.buscaPorLogin(f.getName());
			
			BigDecimal valor = new BigDecimal(0);
			
			Calendar dataInicial = Calendar.getInstance();
			dataInicial.set(Calendar.DAY_OF_MONTH, 1);
			
			Calendar dataFinal = Calendar.getInstance();
			dataFinal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
			
			List<Agendamento> agendamentos = agendamentoService.listarReceitasPorFuncionario(dataInicial, dataFinal, funcionario);
			
			for(Agendamento agendamento : agendamentos){
				valor = agendamento.getValor().add(valor);
			}
			
			return valor;
		}
		
		
		@RequestMapping(method=RequestMethod.GET, value="/listarFuncionarios", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Collection<Funcionario>> listar() {
			Collection<Funcionario> funcionarios = funcionarioService.buscarTodos();
			return new ResponseEntity<>(funcionarios, HttpStatus.OK);
		}
		
		@RequestMapping(method=RequestMethod.GET, value="/listarDentistas", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Collection<Funcionario>> listarDentistas() {
			Collection<Funcionario> funcionarios = funcionarioService.buscarDentistas();
			return new ResponseEntity<>(funcionarios, HttpStatus.OK);
		}
		
		@RequestMapping(method=RequestMethod.POST, value = "/deletarFuncionario")
		public ResponseEntity<Funcionario> excluirfuncionario(@RequestBody Funcionario funcionario) {
			
			Funcionario funcionarioEncontrado = funcionarioService.buscarPorId(funcionario.getIdUsuario());
			
			if(funcionarioEncontrado == null){
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			
			funcionarioService.excluir(funcionarioEncontrado);
			
			return new ResponseEntity<>(HttpStatus.OK);
			
		}
		

	}