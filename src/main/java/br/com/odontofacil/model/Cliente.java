package br.com.odontofacil.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Cliente extends Usuario{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String observacoes;
	
	private String nomeCompletoResponsavel;
	
	private String telefoneContatoResponsavel;
	
	private Boolean maiorIdade;


	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public String getNomeCompletoResponsavel() {
		return nomeCompletoResponsavel;
	}

	public void setNomeCompletoResponsavel(String nomeCompletoResponsavel) {
		this.nomeCompletoResponsavel = nomeCompletoResponsavel;
	}

	public String getTelefoneContatoResponsavel() {
		return telefoneContatoResponsavel;
	}

	public void setTelefoneContatoResponsavel(String telefoneContatoResponsavel) {
		this.telefoneContatoResponsavel = telefoneContatoResponsavel;
	}

	public Boolean getMaiorIdade() {
		return maiorIdade;
	}

	public void setMaiorIdade(Boolean maiorIdade) {
		this.maiorIdade = maiorIdade;
	}

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
