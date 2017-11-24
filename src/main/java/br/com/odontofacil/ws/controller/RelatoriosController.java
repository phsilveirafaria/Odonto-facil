package br.com.odontofacil.ws.controller;

import java.security.Principal;
import java.util.HashMap;
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

import br.com.odontofacil.model.Agendamento;
import br.com.odontofacil.model.Funcionario;
import br.com.odontofacil.model.Orcamento;
import br.com.odontofacil.ws.repository.FuncionarioRepository;
import net.sf.jasperreports.engine.JREmptyDataSource;

@RestController
public class RelatoriosController {
	
	@Autowired
	FuncionarioRepository funcionarioRepository;
	
	
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
	        params.put("endereco", orcamento.getEndereco());
	        params.put("datasource", new JREmptyDataSource());

	        System.out.println("RelatorioController.imprimirRelatorioOrcamento: fim");
	        return new ModelAndView(view, params);	
		} catch(Exception ex) {
			System.out.println("Erro ao gerar relatório: " + ex.getMessage());			
			throw new Exception("Não foi possível gerar o relatório");
		}
	}

}
