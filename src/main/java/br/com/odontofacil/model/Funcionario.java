package br.com.odontofacil.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
public class Funcionario extends Usuario{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@OneToOne
	@JoinColumn(name="idPermissao")
	private Permissao permissao;
	
	private String login;
	
	private byte[] senha;
	
	private byte[] chave;
	
	private String pis;
	
	
	public Permissao getPermissao() {
		return permissao;
	}

	public void setPermissao(Permissao permissao) {
		this.permissao = permissao;
	}

	public String getPis() {
		return pis;
	}

	public void setPis(String pis) {
		this.pis = pis;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	@JsonIgnore
	public byte[] getSenha() {
		return senha;
	}

	public void setSenha(byte[] senha) {
		this.senha = senha;
	}
	@JsonIgnore
	public byte[] getChave() {
		return chave;
	}

	public void setChave(byte[] chave) {
		this.chave = chave;
	}

	
	
}
