package com.ao.fiinikacomercial.model.facturacao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class NotaCredito {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_notaCredito;
	private String nome_cliente;
	private long id_clientefk;
	private String motivo;
	private String origem;
	private String data_emissao;	
	private long id_facturafk;
	private String causa;
	private long id_facturaOrigem;
	private long idEmpresafork;
	
	public long getId_notaCredito() {
		return id_notaCredito;
	}
	public void setId_notaCredito(long id_notaCredito) {
		this.id_notaCredito = id_notaCredito;
	}
	public String getNome_cliente() {
		return nome_cliente;
	}
	public void setNome_cliente(String nome_cliente) {
		this.nome_cliente = nome_cliente;
	}
	public long getId_clientefk() {
		return id_clientefk;
	}
	public void setId_clientefk(long id_clientefk) {
		this.id_clientefk = id_clientefk;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public String getOrigem() {
		return origem;
	}
	public void setOrigem(String origem) {
		this.origem = origem;
	}
	public String getData_emissao() {
		return data_emissao;
	}
	public void setData_emissao(String data_emissao) {
		this.data_emissao = data_emissao;
	}
	public long getId_facturafk() {
		return id_facturafk;
	}
	public void setId_facturafk(long id_facturafk) {
		this.id_facturafk = id_facturafk;
	}
	public String getCausa() {
		return causa;
	}
	public void setCausa(String causa) {
		this.causa = causa;
	}
	public long getId_facturaOrigem() {
		return id_facturaOrigem;
	}
	public void setId_facturaOrigem(long id_facturaOrigem) {
		this.id_facturaOrigem = id_facturaOrigem;
	}
	public long getIdEmpresafork() {
		return idEmpresafork;
	}
	public void setIdEmpresafork(long idEmpresafork) {
		this.idEmpresafork = idEmpresafork;
	}
	
	
	

}
