package br.com.odontofacil.ws.controller;

import java.math.BigDecimal;
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

import br.com.odontofacil.dto.EntradaDespesaDTO;
import br.com.odontofacil.dto.EntradaReceitaDTO;
import br.com.odontofacil.dto.SaidaDespesaDTO;
import br.com.odontofacil.enuns.FormaPagamento;
import br.com.odontofacil.model.Agendamento;
import br.com.odontofacil.model.Despesa;
import br.com.odontofacil.model.Funcionario;
import br.com.odontofacil.util.SalvarEnviarLogs;
import br.com.odontofacil.ws.service.AgendamentoService;
import br.com.odontofacil.ws.service.DespesaService;
import br.com.odontofacil.ws.service.FuncionarioService;

@RestController
public class FinanceiroController {
	
	@Autowired
	FuncionarioService funcionarioService;
	
	@Autowired
	AgendamentoService agendamentoService;
	
	@Autowired
	DespesaService despesaService;
	
	@RequestMapping(
			value="/salvarDespesa",
			method={RequestMethod.POST},
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE
			)
	public SaidaDespesaDTO salvarDespesa(@RequestBody EntradaDespesaDTO EntradaDespesaDTO, Principal user) 
			throws Exception {
		System.out.println("FinanceiroController.salvarDespesa: início");
		
		if (user == null) {
			System.out.println("user nulo");
			throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
		}
		
		Funcionario funcionario = this.funcionarioService.buscaPorLogin(user.getName());
		if (funcionario == null) {
			System.out.println("funcionario nulo em getfuncionarioLogado");
			throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
		}	
		
		Despesa despesa = EntradaDespesaDTO.getDespesa();
		if (despesa == null) {
			System.out.println("despesa null");
			throw new Exception("Não foi possível excluir a despesa!");
		}
		
		if (EntradaDespesaDTO.getDataInicial() == null) {
			System.out.println("dataInicial null");
			throw new Exception("Não foi possível excluir a despesa!");
		}
		
		if (EntradaDespesaDTO.getDataFinal() == null) {
			System.out.println("dataFinal null");
			throw new Exception("Não foi possível excluir a despesa!");
		}
				
		despesa.setFuncionario(funcionario);
		
		try {
			this.despesaService.salvar(despesa);
			SaidaDespesaDTO outDespesaDTO = prepararOutDespesaDTO(EntradaDespesaDTO.getDataInicial(), 
					EntradaDespesaDTO.getDataFinal(), funcionario);
			
			System.out.println("FinanceiroController.salvarDespesa: fim");
			return outDespesaDTO;
		} catch(Exception ex) {
			SalvarEnviarLogs.gravarArquivo(ex);
			System.out.println("Erro ao salvar: " + ex.getMessage());
			throw new Exception("Não foi possível salvar a despesa.");
		}
	}
	
	@RequestMapping(
			value="/carregaFormaPagamento",
			method={RequestMethod.GET},
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public List<FormaPagamento> carregaFormaPagamento() throws Exception {
		
		System.out.println("FinanceitoController.carregaFormaPagamento: início");
		List<FormaPagamento> listFormaPagamento = Arrays.asList(FormaPagamento.values());
		return listFormaPagamento;
	}
	
	
	@RequestMapping(
			value="/excluirDespesa",
			method={RequestMethod.POST},
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE
			)
	public SaidaDespesaDTO excluirDespesa(@RequestBody EntradaDespesaDTO EntradaDespesaDTO, Principal user) 
			throws Exception {
		System.out.println("FinanceiroController.excluirDespesa: início");
		
		if (user == null) {
			System.out.println("user nulo");
			throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
		}
		Funcionario funcionario = this.funcionarioService.buscaPorLogin(user.getName());
		
		Despesa despesa = EntradaDespesaDTO.getDespesa();		
		if (despesa == null) {
			System.out.println("FinanceiroController.excluirDespesa: despesa null");
			throw new Exception("Não foi possível excluir a despesa!");
		}
		
		if (EntradaDespesaDTO.getDataInicial() == null) {
			System.out.println("FinanceiroController.excluirDespesa: dataInicial null");
			throw new Exception("Não foi possível excluir a despesa!");
		}
		
		if (EntradaDespesaDTO.getDataFinal() == null) {
			System.out.println("FinanceiroController.excluirDespesa: dataFinal null");
			throw new Exception("Não foi possível excluir a despesa!");
		}
		
		try {			
			this.despesaService.excluir(despesa);
			SaidaDespesaDTO outDespesaDTO = prepararOutDespesaDTO(EntradaDespesaDTO.getDataInicial(), 
					EntradaDespesaDTO.getDataFinal(), funcionario);
			System.out.println("FinanceiroController.excluirDespesa: fim");
			
			return outDespesaDTO;
		} catch (Exception ex) {
			SalvarEnviarLogs.gravarArquivo(ex);
			System.out.println("FinanceiroController.excluirDespesa: " + ex.getMessage());
			throw new Exception("Não foi possível excluir a despesa!");
		}					
	}
	
	@RequestMapping(
			value="/listarDespesasPorPeriodo",
			method={RequestMethod.GET},
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public SaidaDespesaDTO listarDespesas(@RequestParam("dataInicial") String dataInicial, 
			@RequestParam("dataFinal") String dataFinal, Principal user) throws Exception {
		
		System.out.println("FinanceitoController.listarDespesas: início");
		
		if (user == null) {
			System.out.println("user nulo");
			throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
		}
		Funcionario funcionario = this.funcionarioService.buscaPorLogin(user.getName());
		
		Calendar di = Calendar.getInstance();
		Calendar df = Calendar.getInstance();				
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			di.setTime(format.parse(dataInicial));
			df.setTime(format.parse(dataFinal));
		} catch (ParseException e) {
			System.out.println("Formato de data inválido");
			throw new Exception("Erro ao listar despesas: formato de data inválido.");
		}				
				
		SaidaDespesaDTO outDespesaDTO = prepararOutDespesaDTO(di, df, funcionario);
		System.out.println("FinanceitoController.listarDespesas: fim");
		return outDespesaDTO;
	}
	
	@RequestMapping(
			value = "/listarConsultasPorPeriodo", 
			method={RequestMethod.GET},
			produces = MediaType.APPLICATION_JSON_VALUE			
			)
	public EntradaReceitaDTO listarConsultasPorPeriodo(@RequestParam("dataInicial") String dataInicial, 
			@RequestParam("dataFinal") String dataFinal, Principal user) throws Exception {
		System.out.println("ConsultaController.listarConsultasPorPeriodo: início");
		
		if (user == null) {
			System.out.println("user nulo");
			throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
		}						
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar di = Calendar.getInstance();
		Calendar df = Calendar.getInstance();		
		
		try {
			di.setTime(format.parse(dataInicial));
			df.setTime(format.parse(dataFinal));
		} catch (ParseException e) {
			System.out.println("Formato de data inválido");
			SalvarEnviarLogs.gravarArquivo(e);
			throw new Exception("Erro ao listar agendamentos: formato de data inválido.");
		}		
		
		Funcionario funcionario = this.funcionarioService.buscaPorLogin(user.getName());
		if (funcionario == null) {
			System.out.println("Funcionario nulo em getFuncionarioLogado");
			throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
		}
				
		try {
			EntradaReceitaDTO entradaReceitaDTO = new EntradaReceitaDTO();
			entradaReceitaDTO.setLstAgendamentos(this.agendamentoService.listarConsultasPorPeriodo(di, df));
			
			BigDecimal totalConsultas = new BigDecimal(0);
			for (Agendamento agendamento : entradaReceitaDTO.getLstAgendamentos()) {				
				totalConsultas = totalConsultas.add(agendamento.getValor());
			}
			entradaReceitaDTO.setTotalConsultas(totalConsultas);
			System.out.println("ConsultaController.listarConsultasPorPeriodo: Fim");
		
			return entradaReceitaDTO;
		} catch(Exception ex) {
			SalvarEnviarLogs.gravarArquivo(ex);
			System.out.println("Erro ao listar consultas: " + ex.getMessage());
			throw new Exception("Não foi possível listar as receitas!");
		}
	}
	
	@RequestMapping(
			value = "/listarReceitasPorPeriodo", 
			method={RequestMethod.GET},
			produces = MediaType.APPLICATION_JSON_VALUE			
			)
	public EntradaReceitaDTO listarReceitasPorPeriodo(@RequestParam("dataInicial") String dataInicial, 
			@RequestParam("dataFinal") String dataFinal, Principal user) throws Exception {
		System.out.println("ConsultaController.listarConsultasPorPeriodo: início");
		
		if (user == null) {
			System.out.println("user nulo");
			throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
		}						
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar di = Calendar.getInstance();
		Calendar df = Calendar.getInstance();		
		
		try {
			di.setTime(format.parse(dataInicial));
			df.setTime(format.parse(dataFinal));
		} catch (ParseException e) {
			System.out.println("Formato de data inválido");
			SalvarEnviarLogs.gravarArquivo(e);
			throw new Exception("Erro ao listar agendamentos: formato de data inválido.");
		}		
		
		Funcionario funcionario = this.funcionarioService.buscaPorLogin(user.getName());
		if (funcionario == null) {
			System.out.println("Funcionario nulo em getFuncionarioLogado");
			throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
		}
				
		try {
			EntradaReceitaDTO entradaReceitaDTO = new EntradaReceitaDTO();
			entradaReceitaDTO.setLstAgendamentos(this.agendamentoService.listarReceitasPorPeriodo(di, df));
			
			BigDecimal totalConsultas = new BigDecimal(0);
			for (Agendamento agendamento : entradaReceitaDTO.getLstAgendamentos()) {				
				totalConsultas = totalConsultas.add(agendamento.getConsulta().getValor());
			}
			entradaReceitaDTO.setTotalConsultas(totalConsultas);
			System.out.println("ConsultaController.listarConsultasPorPeriodo: Fim");
		
			return entradaReceitaDTO;
		} catch(Exception ex) {
			System.out.println("Erro ao listar consultas: " + ex.getMessage());
			SalvarEnviarLogs.gravarArquivo(ex);
			throw new Exception("Não foi possível listar as receitas!");
		}
	}
	
	
	@RequestMapping(
			value = "/listarReceitasPorPeriodoDentista", 
			method={RequestMethod.GET},
			produces = MediaType.APPLICATION_JSON_VALUE			
			)
	public BigDecimal listarReceitasPorPeriodoDentista(@RequestParam("dataInicial") String dataInicial, 
			@RequestParam("dataFinal") String dataFinal, Principal user) throws Exception {
		System.out.println("ConsultaController.listarConsultasPorPeriodo: início");
		
		if (user == null) {
			System.out.println("user nulo");
			throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
		}						
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar di = Calendar.getInstance();
		Calendar df = Calendar.getInstance();		
		
		try {
			di.setTime(format.parse(dataInicial));
			df.setTime(format.parse(dataFinal));
		} catch (ParseException e) {
			System.out.println("Formato de data inválido");
			SalvarEnviarLogs.gravarArquivo(e);
			throw new Exception("Erro ao listar agendamentos: formato de data inválido.");
		}		
		
		Funcionario funcionario = this.funcionarioService.buscaPorLogin(user.getName());
		if (funcionario == null) {
			System.out.println("Funcionario nulo em getFuncionarioLogado");
			throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
		}
				
		try {
			EntradaReceitaDTO entradaReceitaDTO = new EntradaReceitaDTO();
			entradaReceitaDTO.setLstAgendamentos(this.agendamentoService.listarReceitasPorPeriodoDentista(di, df, funcionario));
			
			BigDecimal totalConsultas = new BigDecimal(0);
			for (Agendamento agendamento : entradaReceitaDTO.getLstAgendamentos()) {				
				totalConsultas = totalConsultas.add(agendamento.getConsulta().getValor());
			}
			entradaReceitaDTO.setTotalConsultas(totalConsultas);
			System.out.println("ConsultaController.listarConsultasPorPeriodo: Fim");
		
			return totalConsultas;
		} catch(Exception ex) {
			System.out.println("Erro ao listar consultas: " + ex.getMessage());
			SalvarEnviarLogs.gravarArquivo(ex);
			throw new Exception("Não foi possível listar as receitas!");
		}
	}
	
	
	@RequestMapping(
			value = "/listarReceitasPorPeriodoDash", 
			method={RequestMethod.GET},
			produces = MediaType.APPLICATION_JSON_VALUE			
			)
	public BigDecimal listarReceitasPorPeriodoDash(Principal user) throws Exception {
		System.out.println("FinanceiroController.listarReceitasPorPeriodoDash: início");
		
		if (user == null) {
			System.out.println("user nulo");
			throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
		}						
		
		Funcionario funcionario = this.funcionarioService.buscaPorLogin(user.getName());
		if (funcionario == null) {
			System.out.println("Funcionario nulo em getFuncionarioLogado");
			throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
		}
				
		try {
			Calendar data = Calendar.getInstance();
			
			int inicio = data.getActualMinimum(Calendar.DAY_OF_MONTH);
			int fim = data.getActualMaximum(Calendar.DAY_OF_MONTH);
			
			Calendar dataInicio = Calendar.getInstance();
			dataInicio.set(Calendar.DAY_OF_MONTH, 1);
			
			Calendar dataFim = Calendar.getInstance();
			
			List<Agendamento> agendamentos = new ArrayList<Agendamento>();
			agendamentos = agendamentoService.listarReceitasPorPeriodoDash(dataInicio, dataFim);
			
			BigDecimal totalConsultas = new BigDecimal(0);
			for (Agendamento agendamento : agendamentos) {	
				if(agendamento.getValor() != null){
					totalConsultas = totalConsultas.add(agendamento.getValor());
				}
			}
			System.out.println("ConsultaController.listarReceitasPorPeriodoDash: Fim");
		
			return totalConsultas;
		} catch(Exception ex) {
			System.out.println("Erro ao listar consultas: " + ex.getMessage());
			SalvarEnviarLogs.gravarArquivo(ex);
			throw new Exception("Não foi possível listar as receitas!");
		}
	}
	
	@RequestMapping(
			value = "/listarConsultasNaoFinalizadasPorPeriodo", 
			method={RequestMethod.GET},
			produces = MediaType.APPLICATION_JSON_VALUE			
			)
	public List<Agendamento> listarConsultasNaoFinalizadasPorPeriodo(@RequestParam("dataInicial") String dataInicial, 
			@RequestParam("dataFinal") String dataFinal, Principal user) throws Exception {
		System.out.println("ConsultaController.listarConsultasNaoFinalizadasPorPeriodo: início");
		
		if (user == null) {
			System.out.println("user nulo");
			throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
		}						
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar di = Calendar.getInstance();
		Calendar df = Calendar.getInstance();		
		
		try {
			di.setTime(format.parse(dataInicial));
			df.setTime(format.parse(dataFinal));
		} catch (ParseException e) {
			System.out.println("Formato de data inválido");
			SalvarEnviarLogs.gravarArquivo(e);
			throw new Exception("Erro ao listar agendamentos: formato de data inválido.");
		}		
		
		//funcionario funcionario = LoginController.getfuncionarioLogado();
		Funcionario funcionario = this.funcionarioService.buscaPorLogin(user.getName());
		if (funcionario == null) {
			System.out.println("funcionario nulo em getfuncionarioLogado");
			throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
		}
				
		try {			
			List<Agendamento> lstAgendamentos = 
					this.agendamentoService.listarConsultasNaoFinalizadasPorPeriodo(di, df);
		
			System.out.println("ConsultaController.listarConsultasNaoFinalizadasPorPeriodo: Fim");
		
			return lstAgendamentos;
		} catch(Exception ex) {
			System.out.println("Erro ao listar consultas: " + ex.getMessage());
			SalvarEnviarLogs.gravarArquivo(ex);
			throw new Exception("Não foi possível listar as receitas!");
		}
	}
	
	private SaidaDespesaDTO prepararOutDespesaDTO(Calendar dataInicial, Calendar dataFinal, Funcionario funcionario) throws Exception {
		System.out.println("FinanceitoController.prepararDespesaDTO: início");
		//funcionario funcionario = LoginController.getfuncionarioLogado();		
		if (funcionario == null) {
			System.out.println("funcionario nulo em getfuncionarioLogado");
			throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
		}
		
		SaidaDespesaDTO despesaDTO = new SaidaDespesaDTO();		
		despesaDTO.setLstDespesas(this.despesaService.listarPorPeriodo(dataInicial, dataFinal));
		
		BigDecimal totalDespesas = new BigDecimal(0);
		BigDecimal totalDespesasPagas = new BigDecimal(0);
		BigDecimal totalDespesasNaoPagas = new BigDecimal(0);
		
		for (Despesa despesa : despesaDTO.getLstDespesas()) {
			if (despesa.isPago()) {
				totalDespesasPagas = totalDespesasPagas.add(despesa.getValor());				
			} else {
				totalDespesasNaoPagas = totalDespesasNaoPagas.add(despesa.getValor());				
			}
			totalDespesas = totalDespesas.add(despesa.getValor());			
		}		
		despesaDTO.setTotalDespesas(totalDespesas);
		despesaDTO.setTotalDespesasNaoPagas(totalDespesasNaoPagas);
		despesaDTO.setTotalDespesasPagas(totalDespesasPagas);
		
		System.out.println("FinanceitoController.prepararDespesaDTO: fim");
		
		return despesaDTO;
	}
}
