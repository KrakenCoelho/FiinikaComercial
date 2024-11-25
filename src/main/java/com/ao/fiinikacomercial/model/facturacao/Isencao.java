package com.ao.fiinikacomercial.model.facturacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Isencao {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_isencao;
	
	private String codigo;
	@Column(columnDefinition = "TEXT")
	private String descricao;
	private String norma;
	private String mencao;
	public long getId_isencao() {
		return id_isencao;
	}
	public void setId_isencao(long id_isencao) {
		this.id_isencao = id_isencao;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getNorma() {
		return norma;
	}
	public void setNorma(String norma) {
		this.norma = norma;
	}
	public String getMencao() {
		return mencao;
	}
	public void setMencao(String mencao) {
		this.mencao = mencao;
	}
	
	

}
