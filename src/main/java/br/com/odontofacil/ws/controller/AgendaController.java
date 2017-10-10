package br.com.odontofacil.ws.controller;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	    public static String COR_AGENDAMENTO_NAO_COMPARECEU = "#FF5900";
	    
	    private List<Agendamento> listaAgendamentos;
	    
	    
	    
	    @RequestMapping(value = "/listarAgendamentos", method={RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)		
		public List<Agendamento> listarAgendamentos(@RequestParam("dataInicial") String dataInicial, 
				@RequestParam("dataFinal") String dataFinal, Principal user) throws Exception {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar di = Calendar.getInstance();
			Calendar df = Calendar.getInstance();		
			
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
			List<Agendamento> agendamentos = agendamentoService.listarPorPeriodo(di, df, funcionario);
			
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
					
								
			agendamento = agendamentoService.salvar(agendamento);
			
			if (agendamento == null) {
				System.out.println("Erro ao salvar no BD.");
				throw new Exception("Não foi possível salvar o agendamento!");
			}			
			
			if (agendamento.getColor() != null && agendamento.getColor().equals(COR_AGENDAMENTO_NAO_COMPARECEU)) {
				agendamento.setNaoCompareceu(true);
			}		
			
			System.out.println("salvarAgendamento: fim");

			return agendamento;
		}
		

}
