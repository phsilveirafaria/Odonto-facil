package br.com.odontofacil.dto;

import java.math.BigDecimal;
import java.util.List;

import br.com.odontofacil.model.Agendamento;

public class EntradaReceitaDTO {
	
	private List<Agendamento> lstAgendamentos;
	private BigDecimal totalConsultas;
	
	/**
	 * @return the lstAgendamento
	 */
	public List<Agendamento> getLstAgendamentos() {
		return lstAgendamentos;
	}
	
	/**
	 * @param lstAgendamento the lstAgendamento to set
	 */
	public void setLstAgendamentos(List<Agendamento> lstAgendamento) {
		this.lstAgendamentos = lstAgendamento;
	}
	
	/**
	 * @return the totalConsultas
	 */
	public BigDecimal getTotalConsultas() {
		return totalConsultas;
	}
	
	/**
	 * @param totalConsultas the totalConsultas to set
	 */
	public void setTotalConsultas(BigDecimal totalConsultas) {
		this.totalConsultas = totalConsultas;
	}
}
