package br.com.odontofacil.model;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Orcamento {
	
	@Id
	@GeneratedValue
	private long idOrcamento;
	
	private String procedimento;
	
	@ManyToOne
	@JoinColumn(name="idCliente")	
	private Cliente cliente;
	
	@ManyToOne
	@JoinColumn(name="idFuncionario")	
	private Funcionario funcionario;
	
	private BigDecimal valor;
	
	private String cidade = "Porto Alegre";
	
	private String endereco = "Avenida João Antonio da Silveira Nº 2200, sala 907";
	

	public long getIdOrcamento() {
		return idOrcamento;
	}

	public void setIdOrcamento(long idOrcamento) {
		this.idOrcamento = idOrcamento;
	}

	public String getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(String procedimento) {
		this.procedimento = procedimento;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
}
