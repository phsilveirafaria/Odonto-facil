package br.com.odontofacil.ws.controller;

import java.security.Principal;
import java.util.Date;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.odontofacil.model.Funcionario;
import br.com.odontofacil.model.Usuario;
import br.com.odontofacil.util.LoginResponse;
import br.com.odontofacil.ws.service.FuncionarioService;
import br.com.odontofacil.ws.service.LoginService;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class LoginController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private FuncionarioService funcionarioService;

	@RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
	}
	
//	public Funcionario usuarioLogado() {
//	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//	String us = auth.getName();
//	
//	return funcionario;
//	}	
	
	@RequestMapping(method=RequestMethod.GET, value="/userLogado")
	public ResponseEntity<Funcionario> userLogado(@PathParam("{nome}") String nome){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = auth.getName();
		Funcionario funcionario = funcionarioService.buscaPorLogin(user);
		return new ResponseEntity<>(funcionario , HttpStatus.CREATED);
	}
	

	@RequestMapping("/clientes")
	public String irParaClientes() {
		return "pages/cadastroCliente.html";
	}

}
