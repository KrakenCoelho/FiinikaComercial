package com.ao.fiinikacomercial.model.facturacao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Pacote {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_pacote;
	private String nome_pacote;
	private float preco;
	private String tipo;
	private String referencia;
	private int increment;
	private int dias;
	
	
	public int getIncrement() {
		return increment;
	}
	public void setIncrement(int increment) {
		this.increment = increment;
	}
	public long getId_pacote() {
		return id_pacote;
	}
	public void setId_pacote(long id_pacote) {
		this.id_pacote = id_pacote;
	}
	public String getNome_pacote() {
		return nome_pacote;
	}
	public void setNome_pacote(String nome_pacote) {
		this.nome_pacote = nome_pacote;
	}
	public float getPreco() {
		return preco;
	}
	public void setPreco(float preco) {
		this.preco = preco;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public int getDias() {
		return dias;
	}
	public void setDias(int dias) {
		this.dias = dias;
	}
	
	

}
