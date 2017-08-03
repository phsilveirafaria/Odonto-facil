package br.com.odontofacil.ws.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.odontofacil.model.Usuario;
import br.com.odontofacil.ws.repository.UsuarioRepository;
@Service
public class LoginService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public Usuario autenticar(String nome){
		Usuario usuarioAutenticado = usuarioRepository.autenticar(nome);
		return usuarioAutenticado;
	}

}
