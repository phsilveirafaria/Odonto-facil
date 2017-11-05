package br.com.odontofacil.ws.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.odontofacil.model.Orcamento;
import br.com.odontofacil.ws.service.OrcamentoService;

@RestController
public class OrcamentoController {
	
	@Autowired
	OrcamentoService orcamentoService;

	//Serve para inserir e alterar
			@RequestMapping(method=RequestMethod.POST, value="/salvarOrcamentos", consumes=MediaType.APPLICATION_JSON_VALUE)
			public ResponseEntity<Orcamento> salvar(@RequestBody Orcamento orcamento){
				Orcamento orcamentoCadastrado = orcamentoService.salvar(orcamento);
				return new ResponseEntity<>(orcamentoCadastrado, HttpStatus.CREATED);
			}
			
//			@RequestMapping(method=RequestMethod.POST, value="/upload", consumes=MediaType.APPLICATION_JSON_VALUE)
//			public @ResponseBody void upload(@RequestParam("file") MultipartFile file){
//				
//			}
			
			@RequestMapping(method=RequestMethod.GET, value="/listarorcamentos", produces=MediaType.APPLICATION_JSON_VALUE)
			public List<Orcamento> listar() {
				List<Orcamento> orcamentos = orcamentoService.buscarTodos();
				return (orcamentos);
			}
			
			@RequestMapping(method=RequestMethod.POST, value = "/excluirorcamentos")
			public ResponseEntity<Orcamento> excluirorcamento(@RequestBody Orcamento orcamento) {
				
				Orcamento orcamentoEncontrado = orcamentoService.buscarPorId(orcamento.getIdOrcamento());
				
				if(orcamentoEncontrado == null){
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				
				orcamentoService.excluir(orcamentoEncontrado);
				
				return new ResponseEntity<>(HttpStatus.OK);
				
			}
	
}
