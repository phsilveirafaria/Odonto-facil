package br.com.odontofacil.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
public class Funcionario extends Usuario{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name= "id_permissao", nullable = false, updatable = false)
	private Permissao permissao;
	
	private String login;
	
	private byte[] senha;
	
	private byte[] chave;
	
	private String cro;
	
	@Column(name="vinculadogcal")
	private boolean vinculadoGCal;
	
	private boolean ativo;
	
	//private Permissao permissao;
	

	public String getCro() {
		return cro;
	}

	public void setCro(String cro) {
		this.cro = cro;
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

	public Permissao getPermissao() {
		return permissao;
	}

	public void setPermissao(Permissao permissao) {
		this.permissao = permissao;
	}

	public boolean isVinculadoGCal() {
		return vinculadoGCal;
	}

	public void setVinculadoGCal(boolean vinculadoGCal) {
		this.vinculadoGCal = vinculadoGCal;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	
}
