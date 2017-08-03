package br.com.odontofacil.ws.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.odontofacil.model.Usuario;
import br.com.odontofacil.ws.service.UsuarioService;

@RestController
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	//Serve para inserir e alterar
	@RequestMapping(method=RequestMethod.POST, value="/salvar", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Usuario> salvar(@RequestBody Usuario usuario){
		Usuario usuarioCadastrado = usuarioService.salvar(usuario);
		return new ResponseEntity<>(usuarioCadastrado, HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/listar", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Usuario>> listar() {
		Collection<Usuario> usuarios = usuarioService.buscarTodos();
		return new ResponseEntity<>(usuarios, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value = "/deletar/{id}")
	public ResponseEntity<Usuario> excluirUsuario(@PathVariable Long id) {
		
		Usuario usuarioEncontrado = usuarioService.buscarPorId(id);
		
		if(usuarioEncontrado == null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		usuarioService.excluir(usuarioEncontrado);
		
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	

}
