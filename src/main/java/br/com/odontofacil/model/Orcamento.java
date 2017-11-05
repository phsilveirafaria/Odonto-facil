package br.com.odontofacil.model;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Orcamento {
	
	@Id
	@GeneratedValue
	private long idOrcamento;
	
	private String procedimento;
	
	private Cliente cliente;
	
	private Funcionario funcionario;
	
	private Calendar data;
	
	private String cidade;
	
	private String endereco;

	

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

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
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
	
	
}
