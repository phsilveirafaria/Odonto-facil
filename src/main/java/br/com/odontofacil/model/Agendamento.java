package br.com.odontofacil.model;

import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class Agendamento {

	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;	
	
	@ManyToOne
    @JoinColumn(name="idCliente")	
	private Cliente cliente;	
	
	@ManyToOne
    @JoinColumn(name="idFuncionario")	
	private Funcionario funcionario;
	
	@OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="idconsulta")
	private Consulta consulta;	
	
//	@Column(name="idgcalendar") 
//	private String idGCalendar;
//	
//	@Column(name="idrecurring")
//	private String idRecurring;	
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar start;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar end;	
	
	private String descricao;
	
	private String color;
	
	private boolean ativo;	
	
	private String title;
	
	@Transient
	private boolean naoCompareceu;
	
	// Apenas para JPA
	protected Agendamento() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Consulta getConsulta() {
		return consulta;
	}

	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}

	public Calendar getStart() {
		return start;
	}

	public void setStart(Calendar start) {
		this.start = start;
	}

	public Calendar getEnd() {
		return end;
	}

	public void setEnd(Calendar end) {
		this.end = end;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public boolean isNaoCompareceu() {
		return naoCompareceu;
	}

	public void setNaoCompareceu(boolean naoCompareceu) {
		this.naoCompareceu = naoCompareceu;
	}
	
	public String getTitle() {
		if (this.cliente == null) {
			return !this.descricao.isEmpty()?this.descricao:"";
		}
		return (this.descricao != null && !this.descricao.isEmpty()) ?
				this.cliente.getNomeCompleto() + " (" + this.descricao + ")" : 
				this.cliente.getNomeCompleto();
	}
	
	/**
	 * @param titulo the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
