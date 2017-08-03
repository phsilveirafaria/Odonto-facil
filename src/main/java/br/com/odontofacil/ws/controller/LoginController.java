package br.com.odontofacil.ws.controller;

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
	
	
	@RequestMapping(method=RequestMethod.POST, value="/autenticar", consumes=MediaType.APPLICATION_JSON_VALUE)
	public LoginResponse autenticar(@RequestBody Usuario usuario){
		Usuario usuarioAutenticado = loginService.autenticar(usuario.getNomeCompleto());
		
		JwtBuilder builder = Jwts.builder();
		
		String token = builder.setSubject(usuarioAutenticado.getNomeCompleto()) 
		.signWith(SignatureAlgorithm.HS512, "teste")
		.setExpiration(new Date(System.currentTimeMillis() + 1 * 60 * 1000)) // 1 MINUTO
		.compact();
		
		return new LoginResponse(token);
	}
	
	@RequestMapping("/clientes")
	public String irParaClientes(){
		return "pages/cadastroCliente.html";
	}
	
}
