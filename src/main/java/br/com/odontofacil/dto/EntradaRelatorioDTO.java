package br.com.odontofacil.dto;

import java.util.Calendar;

import br.com.odontofacil.model.Cliente;

public class EntradaRelatorioDTO {
	
		private Calendar dataInicial;
		private Calendar dataFinal;
		private Cliente cliente;
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
		
		public Cliente getCliente() {
			return cliente;
		}
		
		public void setCliente(Cliente cliente) {
			this.cliente = cliente;
		}
		
}
