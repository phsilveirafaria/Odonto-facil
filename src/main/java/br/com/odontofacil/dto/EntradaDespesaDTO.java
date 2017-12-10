package br.com.odontofacil.dto;

import java.util.Calendar;

import br.com.odontofacil.model.Despesa;

public class EntradaDespesaDTO {
	
	private Despesa despesa;
	private Calendar dataInicial;
	private Calendar dataFinal;
	private String nomeFuncionario;
	
	/**
	 * @return the despesa
	 */
	public Despesa getDespesa() {
		return despesa;
	}
	
	/**
	 * @param despesa the despesa to set
	 */
	public void setDespesa(Despesa despesa) {
		this.despesa = despesa;
	}
	
	/**
	 * @return the dataInicial
	 */
	public Calendar getDataInicial() {
		return dataInicial;
	}
	
	/**
	 * @param dataInicial the dataInicial to set
	 */
	public void setDataInicial(Calendar dataInicial) {
		this.dataInicial = dataInicial;
	}
	
	/**
	 * @return the dataFinal
	 */
	
	public Calendar getDataFinal() {
		return dataFinal;
	}
	
	/**
	 * @param dataFinal the dataFinal to set
	 */
	public void setDataFinal(Calendar dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getNomeFuncionario() {
		if(despesa.getFuncionario().getNomeCompleto() != null){
			nomeFuncionario = despesa.getFuncionario().getNomeCompleto();
		}
		return nomeFuncionario;
	}

	public void setNomeFuncionario(String nomeFuncionario) {
		this.nomeFuncionario = nomeFuncionario;
	}
	
	

}
