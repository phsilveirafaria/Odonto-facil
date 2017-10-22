package br.com.odontofacil.ws.controller;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;

import br.com.odontofacil.exception.GCalendarException;
import br.com.odontofacil.model.Agendamento;
import br.com.odontofacil.model.Consulta;
import br.com.odontofacil.model.Email;
import br.com.odontofacil.model.Funcionario;
import br.com.odontofacil.util.Util;
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
	    
		/** Application name. */
	    private static final String APPLICATION_NAME = "odontofacil";

	    /** Directory to store user credentials for this application. */
	    private static final java.io.File DATA_STORE_DIR = new java.io.File(
	        System.getProperty("user.home"), ".credentials/odontofacil");

	    /** Global instance of the {@link FileDataStoreFactory}. */
	    private static FileDataStoreFactory DATA_STORE_FACTORY;

	    /** Global instance of the JSON factory. */
	    private static final JsonFactory JSON_FACTORY =
	        JacksonFactory.getDefaultInstance();

	    /** Global instance of the HTTP transport. */
	    private static HttpTransport HTTP_TRANSPORT;
	        

	    /** Global instance of the scopes required by this quickstart.
	     *
	     * If modifying these scopes, delete your previously saved credentials
	     * at ~/.credentials/syspsi
	     */
	    private static final List<String> SCOPES =
	        Arrays.asList(CalendarScopes.CALENDAR,
	        		"https://www.googleapis.com/auth/userinfo.profile");

	    
	    
	    
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
	    @RequestMapping(value = "/removerAgendamento", method={RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
	    public void removerAgendamento(@RequestBody Agendamento agendamento, Principal user) throws Exception {
			System.out.println("removerAgendamento: início");
			if (agendamento == null) {
				System.out.println("Agendamento recebido nulo");
				throw new Exception("Não foi possível remover o agendamento.");
			}
			
			Funcionario funcionario;
			if (user != null) {				
				System.out.println("user.getName(): " + user.getName());
				funcionario = this.funcionarioService.buscaPorLogin(user.getName());
				if (funcionario == null) {
					System.out.println("Funcionario nulo em getPsicologoLogado");
					throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
				}
			} else {
				System.out.println("user nulo em getFuncionarioLogado");
				throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
			}				
			
				if (agendamento.getConsulta() == null) {
					this.agendamentoService.excluir(agendamento);
					System.out.println("Agendamento removido. Id" + agendamento.getId());
				} else {
					// O agendamento possui uma consulta associada. Apenas inativa o agendamento
					System.out.println("Consulta associada. Agendamento marcado como inativo. Id" + agendamento.getId());
				}
			
			
//			if (agendamento.getConsulta() != null) {							
//				agendamento.setAtivo(false);
//				agendamento.getConsulta().setProntuario(Util.encrypt(agendamento.getConsulta().getProntuario(), funcionario));
//				this.agendamentoService.salvar(agendamento);			
//			}
					
			if ((funcionario.isVinculadoGCal()) && (agendamento.getIdGCalendar() != null || agendamento.getIdRecurring() != null)) {
				try {				
					this.excluirAgendamentoNoGoogleCalendar(agendamento, false);
				} catch(GCalendarException ex) {						
				}
			}
			
			System.out.println("removerAgendamento: fim");
		}			
	    
	    
	    private void excluirAgendamentoNoGoogleCalendar(Agendamento agendamento, boolean excluirFuturos) throws GCalendarException {
	    	System.out.println("AgendaController.excluirAgendamentoNoGoogleCalendar: início");
	    	System.out.println("getCalendarService");
	    	com.google.api.services.calendar.Calendar service =
	            getCalendarService();
	    	System.out.println("getCalendarService: OK");
	                       
	    	System.out.println("AgendaController.excluirAgendamentoNoGoogleCalendar: início");
		}
		
	    
	    /**
	     * Build and return an authorized Calendar client service.
	     * @return an authorized Calendar client service
	     * @throws GCalendarException
	     */
	    public static com.google.api.services.calendar.Calendar
	        getCalendarService() throws GCalendarException {
	    	System.out.println("getCalendarService: authorize");
	    	
	        Credential credential = authorize();
	        System.out.println("getCalendarService: authorize sem erros");    	
	        return new com.google.api.services.calendar.Calendar.Builder(
	                HTTP_TRANSPORT, JSON_FACTORY, credential) 
	                .setApplicationName(APPLICATION_NAME)
	                .build();
	    }  
	    
	    /**
	     * Creates an authorized Credential object.
	     * @return an authorized Credential object.
	     * @throws IOException
	     */  
	    public static Credential authorize() throws GCalendarException {        
	    	try {
	    		// Load client secrets.
		        InputStream in =
		            AgendaController.class.getResourceAsStream("/client_secret.json");
		        GoogleClientSecrets clientSecrets =
		            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
		
		        // Build flow and trigger user authorization request.
		        GoogleAuthorizationCodeFlow flow =
		                new GoogleAuthorizationCodeFlow.Builder(
		                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
		                .setDataStoreFactory(DATA_STORE_FACTORY)
		                .setAccessType("offline")
		                .build();

		        Credential credential = new AuthorizationCodeInstalledApp(
		            flow, new LocalServerReceiver()).authorize("user");	        
		        
		        return credential;
	    	} catch(Exception ex) {
	    		System.out.println("Message: " + ex.getMessage());
	    		System.out.println("authorize(): Não foi possível carregar o arquivo client_secret.json.");
	        	throw new GCalendarException("Não foi possível carregar o arquivo client_secret.json.");
	        }
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
