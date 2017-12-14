package br.com.odontofacil.ws.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;
import org.springframework.context.ApplicationContext;

import br.com.odontofacil.dto.EntradaRelatorioDTO;
import br.com.odontofacil.model.Agendamento;
import br.com.odontofacil.model.Despesa;
import br.com.odontofacil.model.Funcionario;
import br.com.odontofacil.model.Orcamento;
import br.com.odontofacil.util.SalvarEnviarLogs;
import br.com.odontofacil.ws.repository.AgendamentoRepository;
import br.com.odontofacil.ws.repository.DespesaRepository;
import br.com.odontofacil.ws.repository.FuncionarioRepository;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@RestController
public class RelatoriosController {
	
	@Autowired
	FuncionarioRepository funcionarioRepository;
	
	@Autowired
	DespesaRepository despesaRepository;
	
	@Autowired
	AgendamentoRepository agendamentoRepository;
	
	
	@Autowired
    private ApplicationContext appContext;	
	
	
	@RequestMapping(
			value = "/imprimirRelatorioOrcamento", 
			method={RequestMethod.POST},
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE
			)
	public ModelAndView imprimirRelatorioOrcamento(@RequestBody Orcamento orcamento, 
			Principal user) throws Exception {	
		System.out.println("RelatorioController.imprimirRelatorioOrcamento: início");								
		
		Funcionario funcionario;
		if (user != null) {			
			System.out.println("user.getName(): " + user.getName());
			funcionario = this.funcionarioRepository.findByLogin(user.getName());
			if (funcionario == null) {
				System.out.println("Funcionario nulo em getfuncionarioLogado");
				throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
			}		
		} else {
			System.out.println("User nulo em getfuncionarioLogado");
			throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
		}	
		
		try {												
			JasperReportsPdfView view = new JasperReportsPdfView();
	        view.setUrl("classpath:br/com/odontofacil/jasper/Orcamento.jrxml");
	        view.setApplicationContext(appContext);
	        view.setContentType("application/pdf");	    
	        view.setReportDataKey("datasource");
	        	        
	        Properties p = new Properties();
	        p.setProperty("Content-disposition", "inline; filename=\"relatorioOrcamento.pdf\"");
	        view.setHeaders(p);

	        Map<String, Object> params = new HashMap<>();
	        params.put("cliente", orcamento.getCliente().getNomeCompleto());	        
	        params.put("procedimento", orcamento.getProcedimento());
	        params.put("idOrcamento", orcamento.getIdOrcamento());
	        params.put("funcionario", orcamento.getFuncionario().getNomeCompleto());
	        params.put("cidade", orcamento.getCidade());
	        params.put("valor", orcamento.getValor());
	        params.put("endereco", orcamento.getEndereco());
	        params.put("datasource", new JREmptyDataSource());

	        System.out.println("RelatorioController.imprimirRelatorioOrcamento: fim");
	        return new ModelAndView(view, params);	
		} catch(Exception ex) {
			System.out.println("Erro ao gerar relatório: " + ex.getMessage());	
			SalvarEnviarLogs.gravarArquivo(ex);
			throw new Exception("Não foi possível gerar o relatório");
		}
	}
	
	
	@RequestMapping(
			value = "/imprimirRelatorioAtestado", 
			method={RequestMethod.POST},
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE
			)
	public ModelAndView imprimirRelatorioAtestado(@RequestBody Agendamento agendamento, 
			Principal user) throws Exception {	
		System.out.println("RelatorioController.imprimirRelatorioAtestado: início");								
		
		Funcionario funcionario;
		if (user != null) {			
			System.out.println("user.getName(): " + user.getName());
			funcionario = this.funcionarioRepository.findByLogin(user.getName());
			if (funcionario == null) {
				System.out.println("Funcionario nulo em getfuncionarioLogado");
				throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
			}		
		} else {
			System.out.println("User nulo em getfuncionarioLogado");
			throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
		}	
		
		try {												
			JasperReportsPdfView view = new JasperReportsPdfView();
	        view.setUrl("classpath:br/com/odontofacil/jasper/Atestado.jrxml");
	        view.setApplicationContext(appContext);
	        view.setContentType("application/pdf");	    
	        view.setReportDataKey("datasource");
	        	        
	        Properties p = new Properties();
	        p.setProperty("Content-disposition", "inline; filename=\"relatorioAtestado.pdf\"");
	        view.setHeaders(p);

	        Map<String, Object> params = new HashMap<>();
	        params.put("cliente", agendamento.getCliente().getNomeCompleto());	        
	        params.put("cpf_cnpj", agendamento.getCliente().getCpf_cnpj());
	        params.put("rua", agendamento.getCliente().getRua());
	        params.put("numero", agendamento.getCliente().getNumero());
	        params.put("datasource", new JREmptyDataSource());

	        System.out.println("RelatorioController.imprimirRelatorioAtestado: fim");
	        return new ModelAndView(view, params);	
		} catch(Exception ex) {
			System.out.println("Erro ao gerar relatório: " + ex.getMessage());	
			SalvarEnviarLogs.gravarArquivo(ex);
			throw new Exception("Não foi possível gerar o relatório");
		}
	}
	
	
	@RequestMapping(
			value = "/imprimirRelatorioDespesas", 
			method={RequestMethod.POST},
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE
			)
	public ModelAndView imprimirRelatorioDespesas(@RequestBody EntradaRelatorioDTO entradaRelatorioDTO, 
			Principal user) throws Exception {	
		System.out.println("RelatorioController.imprimirRelatorioDespesas: início");
					
		Funcionario funcionario;
		if (user != null) {			
			System.out.println("user.getName(): " + user.getName());
			funcionario = this.funcionarioRepository.findByLogin(user.getName());
			if (funcionario == null) {
				System.out.println("Funcionário nulo em getFuncionarioLogado");
				throw new Exception("Erro ao carregar funcionário. Faça login novamente.");
			}		
		} else {
			System.out.println("User nulo em getFuncionarioLogado");
			throw new Exception("Erro ao carregar funcionário. Faça login novamente.");
		}		
		
		try {								
			List<Despesa> lstDespesas = 
					this.despesaRepository.listarPorPeriodo(entradaRelatorioDTO.getDataInicial(), 
							entradaRelatorioDTO.getDataFinal());
			
			BigDecimal totalDespesas = new BigDecimal(0);
			for (Despesa despesa : lstDespesas) {				
				totalDespesas = totalDespesas.add(despesa.getValor()); 
			}
			
								
			JRBeanCollectionDataSource beanColDataSource = 
					new JRBeanCollectionDataSource(lstDespesas);
								
			JasperReportsPdfView view = new JasperReportsPdfView();
	        view.setUrl("classpath:br/com/odontofacil/jasper/Despesas.jrxml");
	        view.setApplicationContext(appContext);
	        view.setContentType("application/pdf");	        
	        view.setReportDataKey("datasource");
	        	        
	        Properties p = new Properties();
	        p.setProperty("Content-disposition", "inline; filename=\"relatorioDespesas.pdf\"");
	        view.setHeaders(p);

	        Map<String, Object> params = new HashMap<>();
	        params.put("dataInicial", entradaRelatorioDTO.getDataInicial());
	        params.put("dataFinal", entradaRelatorioDTO.getDataFinal());
	        params.put("totalDespesas", totalDespesas);
	        params.put("datasource", beanColDataSource);
	        
	        System.out.println("RelatorioController.imprimirRelatorioDespesas: fim");
	        return new ModelAndView(view, params);	        						           	       																						
		} catch(Exception ex) {
			System.out.println("Erro ao gerar relatório: " + ex.getMessage());		
			SalvarEnviarLogs.gravarArquivo(ex);
			throw new Exception("Não foi possível gerar o relatório");
		}
	}
	
		
	
	@RequestMapping(
			value = "/imprimirRelatorioReceitas", 
			method={RequestMethod.POST},
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE
			)
	public ModelAndView imprimirRelatorioReceitas(@RequestBody EntradaRelatorioDTO entradaRelatorioDTO, 
			Principal user) throws Exception {	
		System.out.println("RelatorioController.imprimirRelatorioReceitas: início");
					
		Funcionario funcionario;
		if (user != null) {			
			System.out.println("user.getName(): " + user.getName());
			funcionario = this.funcionarioRepository.findByLogin(user.getName());
			if (funcionario == null) {
				System.out.println("Funcionário nulo em getFuncionaioLogado");
				throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
			}		
		} else {
			System.out.println("User nulo em getFuncionarioLogado");
			throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
		}		
		
		try {								
			List<Agendamento> lstAgendamentos = 
					this.agendamentoRepository.listarConsultasPorPeriodo(
							entradaRelatorioDTO.getDataInicial(), entradaRelatorioDTO.getDataFinal());
			
			BigDecimal totalReceitas = new BigDecimal(0);
			for (Agendamento ag : lstAgendamentos) {				
				totalReceitas = totalReceitas.add(ag.getValor()); 
			}
										
			JRBeanCollectionDataSource beanColDataSource = 
					new JRBeanCollectionDataSource(lstAgendamentos);
								
			JasperReportsPdfView view = new JasperReportsPdfView();
	        view.setUrl("classpath:br/com/odontofacil/jasper/Receitas.jrxml");	        
	        view.setApplicationContext(appContext);
	        view.setContentType("application/pdf");	        
	        view.setReportDataKey("datasource");
	        
	        Properties p = new Properties();
	        p.setProperty("Content-disposition", "inline; filename=\"relatorioReceitas.pdf\"");
	        view.setHeaders(p);

	        Map<String, Object> params = new HashMap<>();
	        params.put("dataInicial", entradaRelatorioDTO.getDataInicial());
	        params.put("dataFinal", entradaRelatorioDTO.getDataFinal());
	        params.put("totalReceitas", totalReceitas);
	        params.put("datasource", beanColDataSource);
	        
	        System.out.println("RelatorioController.imprimirRelatorioReceitas: fim");
	        return new ModelAndView(view, params);	        						           	       																						
		} catch(Exception ex) {
			System.out.println("Erro ao gerar relatório: " + ex.getMessage());
			SalvarEnviarLogs.gravarArquivo(ex);
			throw new Exception("Não foi possível gerar o relatório");
		}
	}
	
	
	@RequestMapping(
			value = "/imprimirImpostoRenda", 
			method={RequestMethod.POST},
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE
			)
	public ModelAndView imprimirImpostoRenda(@RequestBody Agendamento agendamento, 
			Principal user) throws Exception {	
		System.out.println("RelatorioController.imprimirImpostoRenda: início");								
		
		Funcionario funcionario;
		if (user != null) {			
			System.out.println("user.getName(): " + user.getName());
			funcionario = this.funcionarioRepository.findByLogin(user.getName());
			if (funcionario == null) {
				System.out.println("Funcionario nulo em getfuncionarioLogado");
				throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
			}		
		} else {
			System.out.println("User nulo em getfuncionarioLogado");
			throw new Exception("Erro ao carregar funcionario. Faça login novamente.");
		}	
		
		try {												
			JasperReportsPdfView view = new JasperReportsPdfView();
	        view.setUrl("classpath:br/com/odontofacil/jasper/ImpostoRenda.jrxml");
	        view.setApplicationContext(appContext);
	        view.setContentType("application/pdf");	    
	        view.setReportDataKey("datasource");
	        	        
	        Properties p = new Properties();
	        p.setProperty("Content-disposition", "inline; filename=\"relatorioAtestado.pdf\"");
	        view.setHeaders(p);

	        Map<String, Object> params = new HashMap<>();
	        params.put("cliente", agendamento.getCliente().getNomeCompleto());
	        params.put("funcionario", agendamento.getFuncionario().getNomeCompleto());
	        params.put("cro", agendamento.getFuncionario().getCro());
	        params.put("cep", agendamento.getFuncionario().getCep());
	        params.put("valor", agendamento.getValor());
	        params.put("cpf_cnpj", agendamento.getCliente().getCpf_cnpj());
	        params.put("rua", agendamento.getCliente().getRua());
	        params.put("numero", agendamento.getCliente().getNumero());
	        params.put("datasource", new JREmptyDataSource());

	        System.out.println("RelatorioController.imprimirRelatorioAtestado: fim");
	        return new ModelAndView(view, params);	
		} catch(Exception ex) {
			System.out.println("Erro ao gerar relatório: " + ex.getMessage());	
			SalvarEnviarLogs.gravarArquivo(ex);
			throw new Exception("Não foi possível gerar o relatório");
		}
	}
	

}
