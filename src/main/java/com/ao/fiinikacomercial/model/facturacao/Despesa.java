package com.ao.fiinikacomercial.model.facturacao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Despesa {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idDespesa;

    @Column(nullable = false)
    private String tipo_despesa;
    private String nome_despesa;
    private String foto_despesa;
    private long idContafk;
    private long idEmpresafk;
    private String data_despesa;
    private String codigo_despesa;
    private long increment;
    private String criado_por;
    private long idCriadorfk;
    private String custo_despesa;
    private long idFornecedorfk;
    
	public long getIdDespesa() {
		return idDespesa;
	}
	public void setIdDespesa(long idDespesa) {
		this.idDespesa = idDespesa;
	}
	public String getTipo_despesa() {
		return tipo_despesa;
	}
	public void setTipo_despesa(String tipo_despesa) {
		this.tipo_despesa = tipo_despesa;
	}
	public String getNome_despesa() {
		return nome_despesa;
	}
	public void setNome_despesa(String nome_despesa) {
		this.nome_despesa = nome_despesa;
	}
	public String getFoto_despesa() {
		return foto_despesa;
	}
	public void setFoto_despesa(String foto_despesa) {
		this.foto_despesa = foto_despesa;
	}
	public long getIdContafk() {
		return idContafk;
	}
	public void setIdContafk(long idContafk) {
		this.idContafk = idContafk;
	}
	public long getIdEmpresafk() {
		return idEmpresafk;
	}
	public void setIdEmpresafk(long idEmpresafk) {
		this.idEmpresafk = idEmpresafk;
	}
	public String getData_despesa() {
		return data_despesa;
	}
	public void setData_despesa(String data_despesa) {
		this.data_despesa = data_despesa;
	}
	public String getCodigo_despesa() {
		return codigo_despesa;
	}
	public void setCodigo_despesa(String codigo_despesa) {
		this.codigo_despesa = codigo_despesa;
	}
	public long getIncrement() {
		return increment;
	}
	public void setIncrement(long increment) {
		this.increment = increment;
	}
	public String getCriado_por() {
		return criado_por;
	}
	public void setCriado_por(String criado_por) {
		this.criado_por = criado_por;
	}
	public long getIdCriadorfk() {
		return idCriadorfk;
	}
	public void setIdCriadorfk(long idCriadorfk) {
		this.idCriadorfk = idCriadorfk;
	}
	public String getCusto_despesa() {
		return custo_despesa;
	}
	public void setCusto_despesa(String custo_despesa) {
		this.custo_despesa = custo_despesa;
	}
	public long getIdFornecedorfk() {
		return idFornecedorfk;
	}
	public void setIdFornecedorfk(long idFornecedorfk) {
		this.idFornecedorfk = idFornecedorfk;
	}
    
    
    
    
}
