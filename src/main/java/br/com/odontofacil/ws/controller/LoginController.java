package br.com.odontofacil.ws.controller;

import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.odontofacil.model.Usuario;
import br.com.odontofacil.util.LoginResponse;
import br.com.odontofacil.ws.service.LoginService;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	
	@RequestMapping("/user")
	public Principal user(Principal user) {		
		//psicologoLogado = psicologoRepositorio.findByLogin(user.getName());		
		return user;
	}
	
	@RequestMapping("/clientes")
	public String irParaClientes(){
		return "pages/cadastroCliente.html";
	}
	
}
