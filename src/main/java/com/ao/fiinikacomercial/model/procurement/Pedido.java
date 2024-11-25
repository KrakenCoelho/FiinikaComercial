package com.ao.fiinikacomercial.model.procurement;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Pedido {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPedido;
	
	//@ManyToOne(optional=true)
	//@JoinColumn(name="rcliente_id",foreignKey=@ForeignKey(name="cre",foreignKeyDefinition = "FOREIGN KEY (rcliente_id) REFERENCES Cliente(id) ON DELETE CASCADE ON UPDATE CASCADE") )
	@Column
	private Long empresaIdFK;
	
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "YYYY-MM-dd")
	private Date dataInicioConcurso;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "YYYY-MM-dd")  
	private Date dataFechoConcurso;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "YYYY-MM-dd")
	private Date dataCriacao;
	
	//@Column(columnDefinition = "LONGTEXT")
	@Column(length=12000)
	private String descricao;
	
	@Column
	private String estado;
	
	@Column
	private String nomePedido;
	
	@Column
	private String numeroPedido;

	public Pedido() {
		super();
	}

	public Pedido(Long idPedido, Long empresaIdFK, Date dataInicioConcurso, Date dataFechoConcurso, Date dataCriacao,
			String descricao, String estado, String numeroPedido,String nomePedido) {
		super();
		this.idPedido = idPedido;
		this.empresaIdFK = empresaIdFK;
		this.dataInicioConcurso = dataInicioConcurso;
		this.dataFechoConcurso = dataFechoConcurso;
		this.dataCriacao = dataCriacao;
		this.descricao = descricao;
		this.estado = estado;
		this.numeroPedido = numeroPedido;
		this.nomePedido = nomePedido;
	}

	public Long getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(Long idPedido) {
		this.idPedido = idPedido;
	}

	public Long getEmpresaIdFK() {
		return empresaIdFK;
	}

	public void setEmpresaIdFK(Long empresaIdFK) {
		this.empresaIdFK = empresaIdFK;
	}

	public Date getDataInicioConcurso() {
		return dataInicioConcurso;
	}

	public void setDataInicioConcurso(Date dataInicioConcurso) {
		this.dataInicioConcurso = dataInicioConcurso;
	}

	public Date getDataFechoConcurso() {
		return dataFechoConcurso;
	}

	public void setDataFechoConcurso(Date dataFechoConcurso) {
		this.dataFechoConcurso = dataFechoConcurso;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	} 

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public String getNomePedido() {
		return nomePedido;
	}

	public void setNomePedido(String nomePedido) {
		this.nomePedido = nomePedido;
	}
	
	
	
	
	
	
}

