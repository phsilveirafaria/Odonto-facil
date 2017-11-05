package br.com.odontofacil.model;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Despesa {

		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		private long idDespesa;
		@ManyToOne
	    @JoinColumn(name="idFuncionario")	
		private Funcionario funcionario;
		private String descricao;
		private BigDecimal valor;
		private Calendar vencimento;
		private boolean pago;
		private String observacao;
		private int quantidade;
		
		
		public long getIdDespesa() {
			return idDespesa;
		}

		public void setIdDespesa(long idDespesa) {
			this.idDespesa = idDespesa;
		}

		/**
		 * @return the funcionario
		 */
		public Funcionario getFuncionario() {
			return funcionario;
		}
		
		/**
		 * @param funcionario the funcionario to set
		 */
		public void setFuncionario(Funcionario funcionario) {
			this.funcionario = funcionario;
		}
		
		/**
		 * @return the descricao
		 */
		public String getDescricao() {
			return descricao;
		}
		
		/**
		 * @param descricao the descricao to set
		 */
		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
		
		/**
		 * @return the valor
		 */
		public BigDecimal getValor() {
			return valor;
		}
		
		/**
		 * @param valor the valor to set
		 */
		public void setValor(BigDecimal valor) {
			this.valor = valor;
		}
		
		/**
		 * @return the vencimento
		 */
		public Calendar getVencimento() {
			return vencimento;
		}
		
		/**
		 * @param vencimento the vencimento to set
		 */
		public void setVencimento(Calendar vencimento) {
			this.vencimento = vencimento;
		}
		
		/**
		 * @return the pago
		 */
		public boolean isPago() {
			return pago;
		}
		
		/**
		 * @param pago the pago to set
		 */
		public void setPago(boolean pago) {
			this.pago = pago;
		}
		
		/**
		 * @return the observacao
		 */
		public String getObservacao() {
			return observacao;
		}
		
		/**
		 * @param observacao the observacao to set
		 */
		public void setObservacao(String observacao) {
			this.observacao = observacao;
		}

		public int getQuantidade() {
			return quantidade;
		}

		public void setQuantidade(int quantidade) {
			this.quantidade = quantidade;
		}	
		
}
