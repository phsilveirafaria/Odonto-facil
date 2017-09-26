package br.com.odontofacil.ws.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.odontofacil.model.Permissao;
import br.com.odontofacil.ws.service.PermissaoService;

@RestController
public class PermissaoController {

	@Autowired
	PermissaoService permissaoService;
	
	@RequestMapping(method=RequestMethod.GET, value="/listarPermissoes", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Permissao>> listarPermissoes() {
		Collection<Permissao> permissoes = permissaoService.buscarPermissoes();
		return new ResponseEntity<>(permissoes, HttpStatus.OK);
	}
	
}
