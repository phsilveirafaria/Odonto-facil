package br.com.odontofacil.ws.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import br.com.odontofacil.exception.GCalendarEvtNotChangeException;
import br.com.odontofacil.exception.GCalendarException;
import br.com.odontofacil.model.Agendamento;
import br.com.odontofacil.model.Cliente;
import br.com.odontofacil.model.Email;
import br.com.odontofacil.model.Funcionario;
import br.com.odontofacil.model.TmpGCalendarEvent;
import br.com.odontofacil.util.SalvarEnviarLogs;
import br.com.odontofacil.ws.service.AgendamentoService;
import br.com.odontofacil.ws.service.ClienteService;
import br.com.odontofacil.ws.service.FuncionarioService;
import br.com.odontofacil.ws.service.TmpGCalendarEventService;

@RestController
public class AgendaController {

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private AgendamentoService agendamentoService;

	@Autowired
	private TmpGCalendarEventService gCalendarEventService;
	
	private SmsClientController smsController = new SmsClientController();

	public static String COR_AGENDAMENTO_DEFAULT = "#0A6CAC";
	public static String COR_AGENDAMENTO_NAO_COMPARECEU = "#FF0000";
	public static String COR_AGENDAMENTO_FECHADO = "#00FF00";
	public static String password = "odontoFacil2017";

	private List<Agendamento> listaAgendamentos;

	/** Application name. */
	private static final String APPLICATION_NAME = "odontoFacil";

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".credentials/odontofacil");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials at
	 * ~/.credentials/odontoFacil
	 */
	private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR,
			"https://www.googleapis.com/auth/userinfo.profile");

	@RequestMapping(value = "/listarAgendamentos", method = {
			RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
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
			SalvarEnviarLogs.gravarArquivo(e);
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
		if (funcionario.getPermissao().getId() == 1) {
			agendamentos = agendamentoService.listarPorPeriodoePorProfissional(di, df, funcionario);
		} else {
			agendamentos = agendamentoService.listarAllPorPeriodo(di, df);
		}
		System.out.println("listarAgendamentos: fim");
		return agendamentos;
	}

	@RequestMapping(value = "/gCalCallBack", method = {
			RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView gCalendarCallBack(@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "error", required = false) String error, Principal user) {
		System.out.println("AgendaController.gCalendarCallBack: início");

		String redirectView = "#/dashboard";
		if (code != null) {
			try {
				// Load client secrets.
				InputStream in = AgendaController.class.getResourceAsStream("/client_secret.json");
				GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

				// Build flow and trigger user authorization request.
				GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
						clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();

				String redirectUri = "https://localhost:8443/gCalendarCallBack";

				GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
				flow.createAndStoreCredential(response, "user");

				System.out.println("user.getName(): " + user.getName());
				Funcionario funcionario = this.funcionarioService.buscaPorLogin(user.getName());
				funcionario.setVinculadoGCal(true);
				this.funcionarioService.salvar(funcionario);
				redirectView = "#/dashboard?success";
			} catch (Exception ex) {
				System.out.println("MESSAGE: " + ex.getMessage());
				System.out.println("Erro ao vincular calendário: " + ex.getMessage());
				redirectView = "#/dashboard?error";
			}
		} else if (error != null) {
			redirectView = "#/dashboard?error";
		}

		System.out.println("AgendaController.gCalendarCallBack: fim");
		return new ModelAndView(new RedirectView(redirectView, true));
	}


	@RequestMapping(value = "/removerAgendamento", method = {
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	public void removerAgendamento(@RequestBody Agendamento agendamento, Principal user) throws Exception {
		System.out.println("removerAgendamento: início");
		if (agendamento == null) {
			System.out.println("Agendamento recebido nulo");
			throw new Exception("Não foi possível remover o agendamento.");
		}
		agendamentoService.delete(agendamento);		
		
		System.out.println("removerAgendamento: fim");
	}

	/**
	 * Build and return an authorized Calendar client service.
	 * 
	 * @return an authorized Calendar client service
	 * @throws GCalendarException
	 */
	public static com.google.api.services.calendar.Calendar getCalendarService() throws GCalendarException {
		System.out.println("getCalendarService: authorize");

		Credential credential = authorize();
		System.out.println("getCalendarService: authorize sem erros");
		return new com.google.api.services.calendar.Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build();
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	public static Credential authorize() throws GCalendarException {
		try {
			// Load client secrets.
			InputStream in = AgendaController.class.getResourceAsStream("/client_secret.json");
			GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

			// Build flow and trigger user authorization request.
			GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
					clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();

			Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver())
					.authorize("user");

			return credential;
		} catch (Exception ex) {
			System.out.println("Message: " + ex.getMessage());
			SalvarEnviarLogs.gravarArquivo(ex);
			System.out.println("authorize(): Não foi possível carregar o arquivo client_secret.json.");
			throw new GCalendarException("Não foi possível carregar o arquivo client_secret.json.");
		}
	}

	@RequestMapping(value = "/salvarAgendamento", method = {
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Agendamento salvarAgendamento(@RequestBody Agendamento agendamento, Principal user) throws Exception {
		System.out.println("salvarAgendamento: início");

		Funcionario funcionario;

		if (user != null) {
			System.out.println("user.getName(): " + user.getName());

			funcionario = funcionarioService.buscaPorLogin(user.getName());

			if (agendamento.getFuncionario() == null) {
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

		
		if (agendamento.isNaoCompareceu()) {
			agendamento.setColor(COR_AGENDAMENTO_NAO_COMPARECEU);
		}else if(agendamento.getValor() != null){
			agendamento.setFechado(true);
			agendamento.setColor(COR_AGENDAMENTO_FECHADO);
		}else{
			agendamento.setColor(COR_AGENDAMENTO_DEFAULT);
		}

		// Ta, entao depois que ele terminar de salvar, se tudo ocorrer bem
		// nos vamos popular a entidade Email e depois chamar o controller do
		// mesmo modo que eu chamo no Service.

		agendamento = agendamentoService.salvar(agendamento);

		if (agendamento == null) {
			System.out.println("Erro ao salvar no BD.");
			throw new Exception("Não foi possível salvar o agendamento!");
		} else {
			if(agendamento.getStart().getTime().after(new Date())){
			Email email = new Email();
			// Pra quem vai mandar
			email.setTo(agendamento.getCliente().getEmail());
			email.setCc(agendamento.getFuncionario().getEmail());
			// Quem ta mandando
			email.setFrom("tccodontofacil@gmail.com");
			// senha
			email.setPass(password);
			email.setTexto("");
			email.setEmailFormatado("<html>"
					+ "<body>"
					+ "<div style=\"text-align: center;\">"
					+ "<span style=\"font-size:16px;\">Olá, "+ agendamento.getCliente().getNomeCompleto()  +" ,</span></h2>"
					+ "<span style=\"font-size:16px;\">Este &eacute; um e-mail autom&aacute;tico "
							+ "para informar que seu agendamento esta marcado para "+ agendamento.getStart().HOUR_OF_DAY
							+ ", com o Profissional " + agendamento.getFuncionario().getNomeCompleto() + ".</span></h2>"
							+ "<p>"
							+ "<strong><span style=\"font-size:16px;\">Na Odonto F&aacute;cil,"
							+ " avenida João Antônio da Silveira Número 1580.</span></strong></p>");

			// Depois que tu popúlou a entidade EMAIL como foi feito ali encima,
			// tu inicializa o construtor da classe
			// SendEmailController e ela vai "montar" pra ti o email e enviar.
			SendEmailController sendMail = new SendEmailController(email);
			smsController.EnviaSMS(agendamento);
			}
		}

		if (agendamento.getColor() != null && agendamento.getColor().equals(COR_AGENDAMENTO_NAO_COMPARECEU)) {
			agendamento.setNaoCompareceu(true);
		}

		System.out.println("salvarAgendamento: fim");

		return agendamento;
	}

	
	@RequestMapping(value = "/listarAgendamentosDoDia", method = {
			RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
	public int listarAgendamentosDoDia(Principal user) throws Exception {
		System.out.println("AgendaController.listarAgendamentosDoDia: início");
		try {
			Funcionario funcionario;
			if (user != null) {
				funcionario = this.funcionarioService.buscaPorLogin(user.getName());
				if (funcionario == null) {
					System.out.println("Funcionario nulo em getfuncionarioLogado");
					throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
				}
			} else {
				System.out.println("user nulo em getFuncionarioLogado");
				throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
			}

			List<Agendamento> lstAgendamentos = this.agendamentoService.listarAposHorario(funcionario);

			System.out.println("AgendaController.listarAgendamentosDoDia: fim");
			return lstAgendamentos.size();
		} catch (Exception ex) {
			SalvarEnviarLogs.gravarArquivo(ex);
			throw new Exception(ex.getMessage());
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/listarAgendamentosDoDiaGeral", produces=MediaType.APPLICATION_JSON_VALUE)
	public int listarAgendamentosDoDiaGeral() {
		List<Agendamento> agendamentos = agendamentoService.listarAgendamentosDoDiaGeral();
		return (agendamentos.size());
	}
	
	
	@RequestMapping(value = "/listarAgendamentosDoMesPorFuncionario", method = {
			RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
	public int listarAgendamentosDoMesPorFuncionario(Principal user) throws Exception {
		System.out.println("AgendaController.listarAgendamentosDoMesPorFuncionario: início");
		try {
			Funcionario funcionario;
			if (user != null) {
				funcionario = this.funcionarioService.buscaPorLogin(user.getName());
				if (funcionario == null) {
					System.out.println("Funcionario nulo em getfuncionarioLogado");
					throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
				}
			} else {
				System.out.println("user nulo em getFuncionarioLogado");
				throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
			}
			

			Calendar dataInicial = Calendar.getInstance();
			dataInicial.set(Calendar.DAY_OF_MONTH, 1);
			
			Calendar dataFinal = Calendar.getInstance();
			dataFinal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));

			List<Agendamento> lstAgendamentos = this.agendamentoService.listarAgendamentosDoMesPorFuncionario(dataInicial, dataFinal, funcionario);

			System.out.println("AgendaController.listarAgendamentosDoDia: fim");
			return lstAgendamentos.size();
		} catch (Exception ex) {
			SalvarEnviarLogs.gravarArquivo(ex);
			throw new Exception(ex.getMessage());
		}
	}

	@RequestMapping(value = "/exportarAgendamentoParaGoogleCalendar", method = {
			RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
	public void exportarAgendamentoParaGoogleCalendar(Principal user) {
		System.out.println("AgendaController.exportarAgendamentoParaGoogleCalendar: início");
		try {
			System.out.println("user.getName(): " + user.getName());
			Funcionario funcionario = this.funcionarioService.buscaPorLogin(user.getName());
			System.out.println("funcionario: " + funcionario == null ? "Funcionario nulo" : "OK");
			if (funcionario.isVinculadoGCal()) {
				System.out.println("getCalendarService");
				com.google.api.services.calendar.Calendar service = getCalendarService();
				System.out.println("getCalendarService: OK");

				List<Agendamento> lstAgendamentosRepetidos = this.agendamentoService
						.listarAgendamentosRepetidosAVincular(funcionario);

				Map<Long, String> mapAgRepetidos = new HashMap<Long, String>();
				for (Agendamento ag : lstAgendamentosRepetidos) {
					if (ag.getIdGCalendar() == null && ag.getIdRecurring() == null) {
						Calendar now = Calendar.getInstance();
						Calendar start = (Calendar) ag.getStart().clone();
						Calendar origStart = (Calendar) ag.getStart().clone();
						Calendar end = (Calendar) ag.getEnd().clone();

						now.set(Calendar.HOUR, start.get(Calendar.HOUR));
						now.set(Calendar.MINUTE, start.get(Calendar.MINUTE));
						now.set(Calendar.SECOND, 0);
						now.set(Calendar.MILLISECOND, 0);

						if (start.before(now)) {
							boolean naoEncontrou = true;
							for (Calendar dia = now; naoEncontrou; dia.add(Calendar.DATE, 1)) {
								if ((dia.get(Calendar.DAY_OF_WEEK) == ag.getStart().get(Calendar.DAY_OF_WEEK))) {
									start.set(Calendar.DATE, now.get(Calendar.DATE));
									start.set(Calendar.MONTH, now.get(Calendar.MONTH));
									start.set(Calendar.YEAR, now.get(Calendar.YEAR));
									end.set(Calendar.DATE, now.get(Calendar.DATE));
									end.set(Calendar.MONTH, now.get(Calendar.MONTH));
									end.set(Calendar.YEAR, now.get(Calendar.YEAR));
									break;
								}
							}
						}

						Agendamento tmpAgendamento = new Agendamento(ag.getCliente(), ag.getIdGCalendar(),
								ag.getIdRecurring(), start, end, ag.getDescricao(), ag.getColor(), ag.isAtivo());

						Agendamento agendamento = this.salvarAgendamentoNoGoogleCalendar(tmpAgendamento, funcionario,
								service);

						List<Agendamento> lstAgendamentosAnteriores = this.agendamentoService
								.listarAgendamentosRepetitivosParaNaoVincular(funcionario, now);

						for (Agendamento agend : lstAgendamentosAnteriores) {
							agend.setIdRecurring(ag.getIdRecurring());
						}
						this.agendamentoService.salvarListaAgendamentos(lstAgendamentosAnteriores);

						this.excluirAgendamentosNoGoogleCalendarDuranteExportacao(agendamento, funcionario, service);
					}
				}
				if (lstAgendamentosRepetidos != null && !lstAgendamentosRepetidos.isEmpty()) {
					System.out.println("Salvando lista eventos repetidos");
					this.agendamentoService.salvarListaAgendamentos(lstAgendamentosRepetidos);
				}

				List<Agendamento> lstAgendamentosSimples = this.agendamentoService
						.listarAgendamentosSimplesAVincular(funcionario, Calendar.getInstance());

				String idRecurring;
				for (Agendamento ag : lstAgendamentosSimples) {

					Agendamento agendamento = this.salvarAgendamentoNoGoogleCalendar(ag, funcionario, service);
					ag.setIdGCalendar(agendamento.getIdGCalendar());
					ag.setIdRecurring(agendamento.getIdRecurring());
				}

				if (lstAgendamentosSimples != null && !lstAgendamentosSimples.isEmpty()) {
					System.out.println("Salvando lista eventos simples");
					this.agendamentoService.salvarListaAgendamentos(lstAgendamentosSimples);
				}
			}
		} catch (Exception ex) {
			SalvarEnviarLogs.gravarArquivo(ex);
			System.out.println("Erro ao vincular: " + ex.getMessage());
		}
		System.out.println("AgendaController.exportarAgendamentoParaGoogleCalendar: fim");
	}

	@RequestMapping(value = "/desvincularAgendamentosDoGoogleCalendar", method = {
			RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
	public void desvincularAgendamentosDoGoogleCalendar(Principal user) {
		System.out.println("AgendaController.desvincularAgendamentosDoGoogleCalendar: início");
		try {
			System.out.println("user.getName(): " + user.getName());
			Funcionario funcionario = this.funcionarioService.buscaPorLogin(user.getName());
			if (funcionario.isVinculadoGCal()) {
				funcionario.setVinculadoGCal(false);
				this.funcionarioService.salvar(funcionario);
				System.out.println("funcionario: " + funcionario == null ? "Erro" : "OK");
				List<Agendamento> lstAgendamentos = this.agendamentoService.listarAgendamentosVinculados(funcionario);
				for (Agendamento ag : lstAgendamentos) {
					ag.setIdGCalendar(null);
					ag.setIdRecurring(null);
				}
				System.out.println("Salvando lista");
				this.agendamentoService.salvarListaAgendamentos(lstAgendamentos);
			}
		} catch (Exception ex) {
			SalvarEnviarLogs.gravarArquivo(ex);
			System.out.println("Erro ao desvincular: " + ex.getMessage());
		}
		System.out.println("AgendaController.desvincularAgendamentosDoGoogleCalendar: fim");
	}

	private Agendamento salvarAgendamentoNoGoogleCalendar(Agendamento agendamento, Funcionario funcionario,
			com.google.api.services.calendar.Calendar service) throws GCalendarException {
		System.out.println("AgendaController.salvarAgendamentoNoGoogleCalendar: início");
		try {
			Event event = new Event().setSummary(agendamento.getCliente().getNomeCompleto())
					.setDescription(agendamento.getDescricao());

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-03:00");
			DateTime startDateTime = new DateTime(format.format(agendamento.getStart().getTime()));
			EventDateTime start = new EventDateTime().setDateTime(startDateTime).setTimeZone("America/Sao_Paulo");
			event.setStart(start);

			DateTime endDateTime = new DateTime(format.format(agendamento.getEnd().getTime()));
			EventDateTime end = new EventDateTime().setDateTime(endDateTime).setTimeZone("America/Sao_Paulo");
			event.setEnd(end);

			event = service.events().insert("primary", event).execute();
			agendamento.setIdGCalendar(event.getId());

			// Apenas para novos agendamentos
			if (agendamento != null) {
				Calendar maxDateCal = Calendar.getInstance();
				maxDateCal.setTime(agendamento.getStart().getTime());
				maxDateCal.add(Calendar.DATE, 1);
				DateTime maxDate = new DateTime(format.format(maxDateCal.getTime()));
				Events events = service.events().instances("primary", event.getId()).setMaxResults(1)
						.setTimeMax(maxDate).setPageToken(null).execute();

				if (events.getItems() != null && !events.getItems().isEmpty()) {
					Event evt = events.getItems().get(0);
					agendamento.setIdGCalendar(evt.getId());
					agendamento.setIdRecurring(evt.getRecurringEventId());
				}

			}

			System.out.println("AgendaController.salvarAgendamentoNoGoogleCalendar: fim");
			return agendamento;
		} catch (Exception e) {
			SalvarEnviarLogs.gravarArquivo(e);
			System.out.println("Erro ao salvar no google calendar. Id agendamento: " + agendamento.getId() + " erro: "
					+ e.getMessage());
			throw new GCalendarException("Erro ao salvar agendamento no Google Calendar");
		}
	}

	private void excluirAgendamentoNoGoogleCalendar(Agendamento agendamento, boolean excluirFuturos)
			throws GCalendarException {
		System.out.println("AgendaController.excluirAgendamentoNoGoogleCalendar: início");
		System.out.println("getCalendarService");
		com.google.api.services.calendar.Calendar service = getCalendarService();
		System.out.println("getCalendarService: OK");

		try {
			if (excluirFuturos) {
				if (agendamento != null) {
					// Aguardar API com opção de excluir eventos futuros
				} else {
					service.events().delete("primary", agendamento.getIdRecurring()).execute();
					System.out.println("Série eventos excluídos no GCal: " + agendamento.getIdRecurring());
				}
			} else {
				service.events().delete("primary", agendamento.getIdGCalendar()).execute();
				System.out.println("Evento excluído no GCal: " + agendamento.getIdGCalendar());
			}
		} catch (Exception e) {
			System.out.println("Erro ao excluir no google calendar. Id agendamento: " + agendamento.getId() + " erro: "
					+ e.getMessage());
			SalvarEnviarLogs.gravarArquivo(e);
			throw new GCalendarException("Erro ao excluir agendamento no Google Calendar");
		}
		System.out.println("AgendaController.excluirAgendamentoNoGoogleCalendar: início");
	}

	/**
	 * Exclui eventos do Google Calendar durante a exportação (quando da
	 * ativação da integração com o GCal) de uma série de eventos repetidos do
	 * sistema no qual exista algum evento desativado
	 * 
	 * @param agendamentoPrincipal
	 *            o agendamento com evento principal igual a true
	 * @throws GCalendarException
	 *             caso algum problema ocorra
	 * @throws IOException
	 */
	private void excluirAgendamentosNoGoogleCalendarDuranteExportacao(Agendamento agendamentoPrincipal,
			Funcionario funcionario, com.google.api.services.calendar.Calendar service)
			throws IOException, GCalendarException {
		System.out.println("AgendaController.excluirAgendamentoNoGoogleCalendarDuranteExportacao: início");

		List<Agendamento> lstAgendamentos = this.agendamentoService
				.listarAgendamentosParaExcluirNoGoogleCalendarDuranteExportacao(agendamentoPrincipal,
						funcionario);

		if (lstAgendamentos != null && !lstAgendamentos.isEmpty()) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-03:00");
			for (Agendamento ag : lstAgendamentos) {
				Calendar maxDateCal = Calendar.getInstance();
				Calendar minDateCal = Calendar.getInstance();
				maxDateCal.setTime(ag.getStart().getTime());
				minDateCal.setTime(ag.getStart().getTime());
				maxDateCal.add(Calendar.DATE, 1); // timeMax é exclusivo
				DateTime maxDate = new DateTime(format.format(maxDateCal.getTime()));
				DateTime minDate = new DateTime(format.format(minDateCal.getTime()));

				Events lstEventToDelete = service.events().instances("primary", agendamentoPrincipal.getIdRecurring())
						.setTimeMin(minDate).setTimeMax(maxDate).execute();

				if (lstEventToDelete != null && !lstEventToDelete.getItems().isEmpty()) {
					Event toDelete = lstEventToDelete.getItems().get(0);
					service.events().delete("primary", toDelete.getId()).execute();
					System.out.println("Evento excluído no GCal: " + toDelete.getId());
				}
			}
		}

		System.out.println("AgendaController.excluirAgendamentoNoGoogleCalendarDuranteExportacao: fim");
	}

	private Agendamento editarAgendamentoNoGoogleCalendar(Agendamento agendamento,
			com.google.api.services.calendar.Calendar service) throws GCalendarException {
		try {
			Event event = service.events().get("primary", agendamento.getIdGCalendar()).execute();
			event.setSummary(agendamento.getCliente().getNomeCompleto());
			event.setDescription(agendamento.getDescricao());

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-03:00");
			DateTime startDateTime = new DateTime(format.format(agendamento.getStart().getTime()));
			EventDateTime start = new EventDateTime().setDateTime(startDateTime).setTimeZone("America/Sao_Paulo");
			event.setStart(start);

			DateTime endDateTime = new DateTime(format.format(agendamento.getEnd().getTime()));
			EventDateTime end = new EventDateTime().setDateTime(endDateTime).setTimeZone("America/Sao_Paulo");
			event.setEnd(end);

			event = service.events().update("primary", event.getId(), event).execute();

			if (agendamento != null) {
				Calendar maxDateCal = Calendar.getInstance();
				maxDateCal.setTime(agendamento.getStart().getTime());
				maxDateCal.add(Calendar.DATE, 1);
				DateTime maxDate = new DateTime(format.format(maxDateCal.getTime()));
				Events events = service.events().instances("primary", event.getId()).setMaxResults(1)
						.setTimeMax(maxDate).setPageToken(null).execute();

				if (events.getItems() != null) {
					Event evt = events.getItems().get(0);
					agendamento.setIdGCalendar(evt.getId());
					agendamento.setIdRecurring(evt.getRecurringEventId());
				}

			}

			System.out.println("AgendaController.editarAgendamentoNoGoogleCalendar: início");
			return agendamento;
		} catch (Exception e) {
			System.out.println("Erro ao editar no google calendar. Id agendamento: " + agendamento.getId() + " erro: "
					+ e.getMessage());
			SalvarEnviarLogs.gravarArquivo(e);
			throw new GCalendarException("Erro ao editar agendamento no Google Calendar");
		}
	}

	private TmpGCalendarEvent verificarAlteracoesGCal(Event event, TmpGCalendarEvent tmpGCalendarEvent)
			throws GCalendarEvtNotChangeException {
		boolean change = false;

		if (event.getStart().getDateTime().getValue() != tmpGCalendarEvent.getStart().getTimeInMillis()) {
			Calendar startEvent = Calendar.getInstance();
			startEvent.setTimeInMillis(event.getStart().getDateTime().getValue());
			tmpGCalendarEvent.setStart(startEvent);
			change = true;
		}

		if (event.getEnd().getDateTime().getValue() != tmpGCalendarEvent.getEnd().getTimeInMillis()) {
			Calendar endEvent = Calendar.getInstance();
			endEvent.setTimeInMillis(event.getEnd().getDateTime().getValue());
			tmpGCalendarEvent.setEnd(endEvent);
			change = true;
		}

		if (event.getSummary() != null && !event.getSummary().equals(tmpGCalendarEvent.getSummary())) {
			tmpGCalendarEvent.setSummary(event.getSummary());
			change = true;
		}

		if (!change) {
			throw new GCalendarEvtNotChangeException();
		}

		return tmpGCalendarEvent;
	}

	private Agendamento verificarAlteracoesGCal(Event event, Agendamento agendamento, Funcionario funcionario)
			throws GCalendarEvtNotChangeException, IOException, GCalendarException {
		boolean change = false;

		if (event.getStart().getDateTime().getValue() != agendamento.getStart().getTimeInMillis()) {
			Calendar startEvent = Calendar.getInstance();
			startEvent.setTimeInMillis(event.getStart().getDateTime().getValue());
			agendamento.setStart(startEvent);
			change = true;
		}

		if (event.getEnd().getDateTime().getValue() != agendamento.getEnd().getTimeInMillis()) {
			Calendar endEvent = Calendar.getInstance();
			endEvent.setTimeInMillis(event.getEnd().getDateTime().getValue());
			agendamento.setEnd(endEvent);
			change = true;
		}

		if (event.getDescription() != null && !event.getDescription().equals(agendamento.getDescricao())) {
			agendamento.setDescricao(event.getDescription());
			change = true;
		}

		if (!change) {
			throw new GCalendarEvtNotChangeException();
		}

		return agendamento;
	}
}
