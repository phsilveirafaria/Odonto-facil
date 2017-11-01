package br.com.odontofacil.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import br.com.odontofacil.enuns.FormaPagamento;

@Entity
public class Receita {
	
	@Id
	@GeneratedValue
	private Long idReceita;
	
	@ManyToOne
	private Funcionario funcionario;
	
	private BigDecimal valor;
	
	@OneToOne
	@JoinColumn(name="idAgendamento")	
	private Agendamento agendamento;
	
	private String descricao;
	
	private FormaPagamento formaPagamento;

	public Long getId() {
		return idReceita;
	}

	public void setId(Long idReceita) {
		this.idReceita = idReceita;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	
	public Agendamento getAgendamento() {
		return agendamento;
	}

	public void setAgendamento(Agendamento agendamento) {
		this.agendamento = agendamento;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}
	
	
}
