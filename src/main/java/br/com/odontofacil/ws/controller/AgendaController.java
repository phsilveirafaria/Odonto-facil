package br.com.odontofacil.ws.controller;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.odontofacil.model.Agendamento;
import br.com.odontofacil.model.Consulta;
import br.com.odontofacil.model.Email;
import br.com.odontofacil.model.Funcionario;
import br.com.odontofacil.ws.service.AgendamentoService;
import br.com.odontofacil.ws.service.FuncionarioService;

@RestController
public class AgendaController {
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private AgendamentoService agendamentoService;
	
		public static String COR_AGENDAMENTO_DEFAULT = "#0A6CAC";
	    public static String COR_AGENDAMENTO_NAO_COMPARECEU = "#FF0000";
	    
	    private List<Agendamento> listaAgendamentos;
	    
	    
	    
	    @RequestMapping(value = "/listarAgendamentos", method={RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)		
		public List<Agendamento> listarAgendamentos(@RequestParam("dataInicial") String dataInicial, 
				@RequestParam("dataFinal") String dataFinal, Principal user) throws Exception {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar di = Calendar.getInstance();
			Calendar df = Calendar.getInstance();
			List<Agendamento> agendamentos = new ArrayList<Agendamento>();
			
			try {
				di.setTime(format.parse(dataInicial));
				df.setTime(format.parse(dataFinal));
			} catch (ParseException e) {
				throw new Exception("Erro ao listar agendamentos: formato de data inválido.");
			}		
			
			Funcionario funcionario;
			if (user != null) {			
				funcionario = this.funcionarioService.buscaPorLogin(user.getName());
				if (funcionario == null) {
					System.out.println("Funcionario nulo em getFuncionarioLogado");
					throw new Exception("Erro ao carregar Funcionario. Faça login novamente.");
				}		
			} else {
				System.out.println("User nulo em getFuncionarioLogado");
				throw new Exception("Erro ao carregar Funcionario. Faça login novamente.");
			}
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");	
			if(funcionario.getPermissao().getId() == 1){
				agendamentos = agendamentoService.listarPorPeriodoePorProfissional(di, df, funcionario);
			}else{
				agendamentos = agendamentoService.listarAllPorPeriodo(di, df);	
			}
//			for (Agendamento ag : agendamentos) {
//				if (ag.isAtivo()) {
//					if (ag.getColor().equals(COR_AGENDAMENTO_NAO_COMPARECEU)) {
//						ag.setNaoCompareceu(true);
//					}
//					
////					// Decripta dados do prontuário				
////					if (ag.getConsulta() != null && ag.getConsulta().getProntuario() != null && !ag.getConsulta().getProntuario().isEmpty()) {
////						ag.getConsulta().setProntuario(Util.decrypt(ag.getConsulta().getProntuario(), psicologo));
////					}
//					
//					listaAgendamentos.add(ag);
//				}
//			}												
			
			System.out.println("listarAgendamentos: fim");
			return agendamentos;		
		}
	    
//	    @RequestMapping(value = "/agendamentosDoMes", 
//				method={RequestMethod.POST}, 
//				produces = MediaType.APPLICATION_JSON_VALUE,
//				consumes = MediaType.APPLICATION_JSON_VALUE
//				)
//		public int agendamentosDoMes() throws Exception {
//			
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//			Calendar di = Calendar.getInstance();
//			Calendar df = Calendar.getInstance();
//			
//			try {
//				di.setTime(format.parse(dataInicial));
//				df.setTime(format.parse(dataFinal));
//			} catch (ParseException e) {
//				throw new Exception("Erro ao listar agendamentos: formato de data inválido.");
//			}	
//			
//			return agendamentoService.agendamentosDoMes(di, df);
//	    }
		
		
		@RequestMapping(value = "/salvarAgendamento", 
				method={RequestMethod.POST}, 
				produces = MediaType.APPLICATION_JSON_VALUE,
				consumes = MediaType.APPLICATION_JSON_VALUE
				)
		public Agendamento salvarAgendamento(@RequestBody Agendamento agendamento, Principal user) throws Exception {
			System.out.println("salvarAgendamento: início");		

			Funcionario funcionario;
			Consulta consulta;
			
			if (user != null) {
				System.out.println("user.getName(): " + user.getName());
				
				funcionario = funcionarioService.buscaPorLogin(user.getName());
				
				if(agendamento.getFuncionario() == null){
					agendamento.setFuncionario(funcionario);
				}
				
				if (funcionario == null) {
					System.out.println("Funcionario nulo em getFuncionarioLogado");
					
					throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
				}
			} else {
				System.out.println("user nulo em getFuncionarioLogado");
				throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
			}
			
			if (agendamento == null) {
				System.out.println("Agendamento recebido nulo.");
				throw new Exception("Não foi possível salvar o agendamento!");
			}		
			
			agendamento.setColor(COR_AGENDAMENTO_DEFAULT);
			if (agendamento.isNaoCompareceu()) {
				agendamento.setColor(COR_AGENDAMENTO_NAO_COMPARECEU);
			}
					
			//Ta, entao depois que ele terminar de salvar, se tudo ocorrer bem
			//nos vamos popular a entidade Email e depois chamar o controller do mesmo modo que eu chamo no Service.
			
			agendamento = agendamentoService.salvar(agendamento);
			
			if (agendamento == null) {
				System.out.println("Erro ao salvar no BD.");
				throw new Exception("Não foi possível salvar o agendamento!");
			} else {
				/*
				 * Isso tu pode fazer dum jeito melhor e mais organizar depois... aqui vou fazer só pra mostrar
				 */
				Email email = new Email();
				//Pra que vai mandar
				email.setTo("contato@keepupload.com");
				//Quem ta mandando
				email.setFrom("contato@keepupload.com");
				//senha
				email.setPass("SENHA");
				//Texto do email... o ideal é tu ir num "montador de HTML" e montar o texto.
				email.setEmailFormatado("<h2 style=\"text-align: left;\">Nome: "+email.getNome()+"</h2>"
										+"<h2 style=\"text-align: left;\">Email: "+email.getEmail()+"</h2>"
										+"<h2 style=\"text-align: center;\">Texto&nbsp;</h2>"
										+"<h2 style=\"text-align: center;\">"+email.getTexto()+"</h2>");
				
				//Depois que tu popúlou a entidade EMAIL como foi feito ali encima, tu inicializa o construtor da classe
				//SendEmailController e ela vai "montar" pra ti o email e enviar.
				SendEmailController sendMail = new SendEmailController(email);
			}
			
			if (agendamento.getColor() != null && agendamento.getColor().equals(COR_AGENDAMENTO_NAO_COMPARECEU)) {
				agendamento.setNaoCompareceu(true);
			}		
			
			System.out.println("salvarAgendamento: fim");

			return agendamento;
		}
		

}
