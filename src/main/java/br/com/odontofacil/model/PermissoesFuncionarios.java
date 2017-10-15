package br.com.odontofacil.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

	public class PermissoesFuncionarios {
		@Id
		Long id;
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
		
		/**
		 * @param funcionario the funcionario to set
		 */
		public Funcionario getFuncionario() {
			return funcionario;
		}
		public Long getId() {
			return id;
		}

		/**
		 * @return the permissao
		 */
		public Permissao getPermissao() {
			return permissao;
		}
		/**
		 * @return the dataCriacao
		 */
		public Calendar getDataCriacao() {
			return dataCriacao;
		}
	}