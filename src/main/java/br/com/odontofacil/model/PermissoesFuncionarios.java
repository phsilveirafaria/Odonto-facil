package br.com.odontofacil.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

	@Entity
	public class PermissoesFuncionarios {
		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		Integer id;
		@ManyToOne
	    @JoinColumn(name="id_Usuario")
		Funcionario funcionario;
		@ManyToOne
	    @JoinColumn(name="id_permissao")
		Permissao permissao;
		@Column(name="datacriacao")
		Calendar dataCriacao;		
		
		/**
		 * @param id
		 * @param funcionario
		 * @param permissao
		 * @param dataCriacao
		 */
		public PermissoesFuncionarios(Funcionario funcionario, Permissao permissao, Calendar dataCriacao) {
			super();		
			this.funcionario = funcionario;
			this.permissao = permissao;
			this.dataCriacao = dataCriacao;
		}
		/**
		 * @return the id
		 */
		public Integer getId() {
			return id;
		}
		/**
		 * @param id the id to set
		 */
		public void setId(Integer id) {
			this.id = id;
		}
		
		/**
		 * @param funcionario the funcionario to set
		 */
		public Funcionario getFuncionario() {
			return funcionario;
		}
		public void setFuncionario(Funcionario funcionario) {
			this.funcionario = funcionario;
		}
		/**
		 * @return the permissao
		 */
		public Permissao getPermissao() {
			return permissao;
		}
		/**
		 * @param permissao the permissao to set
		 */
		public void setPermissao(Permissao permissao) {
			this.permissao = permissao;
		}
		/**
		 * @return the dataCriacao
		 */
		public Calendar getDataCriacao() {
			return dataCriacao;
		}
		/**
		 * @param dataCriacao the dataCriacao to set
		 */
		public void setDataCriacao(Calendar dataCriacao) {
			this.dataCriacao = dataCriacao;
		}
	}
