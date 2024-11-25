package com.ao.fiinikacomercial.model.facturacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Factura {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_factura;

    @Column(nullable = false)
    private String codigo_factura;
    private String data_emissao;
    private String data_expiracao;
    private float desconto;
    private float imposto;
    private long increment;
    private long increment_fact;
    private long id_empresafk;
    private long id_clientefk;
    private String estado;
    private String total;
    private String total_final;
    private String data_hora;
    private String taxa;
    private String hash_msg;
    private String tipo;
    private String referente;
    private String vendedor;
    private long id_contafk;
    private String obs;
    private  String code_factura;
    private long id_vendedorfk;
    private  String valor_desconto;
    private  String impos_retido;
    private String url_pdf;
   
    
    
    
    
    
	public String getTaxa() {
		return taxa;
	}
	public void setTaxa(String taxa) {
		this.taxa = taxa;
	}

	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getTotal_final() {
		return total_final;
	}
	public void setTotal_final(String total_final) {
		this.total_final = total_final;
	}
	public long getId_clientefk() {
		return id_clientefk; 
	}
	public void setId_clientefk(long id_clientefk) {
		this.id_clientefk = id_clientefk;
	}
	public String getCodigo_factura() {
		return codigo_factura;
	}
	public void setCodigo_factura(String codigo_factura) {
		this.codigo_factura = codigo_factura;
	}
	public float getIncrement() {
		return increment;
	}
	public void setIncrement(long increment) {
		this.increment = increment;
	}
	public long getId_empresafk() {
		return id_empresafk;
	}
	public void setId_empresafk(long id_empresafk) {
		this.id_empresafk = id_empresafk;
	}
	public long getId_factura() {
		return id_factura;
	}
	public void setId_factura(long id_factura) {
		this.id_factura = id_factura;
	}
	
	public String getData_emissao() {
		return data_emissao;
	}
	public void setData_emissao(String data_emissao) {
		this.data_emissao = data_emissao;
	}
	public String getData_expiracao() {
		return data_expiracao;
	}
	public void setData_expiracao(String data_expiracao) {
		this.data_expiracao = data_expiracao;
	}
	
	public float getDesconto() {
		return desconto;
	}
	public void setDesconto(float desconto) {
		this.desconto = desconto;
	}
	public float getImposto() {
		return imposto;
	}
	public void setImposto(float imposto) {
		this.imposto = imposto;
	}
	public String getData_hora() {
		return data_hora;
	}
	public void setData_hora(String data_hora) {
		this.data_hora = data_hora;
	}
	public String getHash_msg() {
		return hash_msg;
	}
	public void setHash_msg(String hash_msg) {
		this.hash_msg = hash_msg;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public long getIncrement_fact() {
		return increment_fact;
	}
	public String getReferente() {
		return referente;
	}
	public void setReferente(String referente) {
		this.referente = referente;
	}
	public void setIncrement_fact(long increment_fact) {
		this.increment_fact = increment_fact;
	}
	public String getVendedor() {
		return vendedor;
	}
	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}
	public long getId_contafk() {
		return id_contafk;
	}
	public void setId_contafk(long id_contafk) {
		this.id_contafk = id_contafk;
	}
	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
	public String getCode_factura() {
		return code_factura;
	}
	public void setCode_factura(String code_factura) {
		this.code_factura = code_factura;
	}
	public long getId_vendedorfk() {
		return id_vendedorfk;
	}
	public void setId_vendedorfk(long id_vendedorfk) {
		this.id_vendedorfk = id_vendedorfk;
	}
	public String getValor_desconto() {
		return valor_desconto;
	}
	public void setValor_desconto(String valor_desconto) {
		this.valor_desconto = valor_desconto;
	}
	public String getImpos_retido() {
		return impos_retido;
	}
	public void setImpos_retido(String impos_retido) {
		this.impos_retido = impos_retido;
	}
	public String getUrl_pdf() {
		return url_pdf;
	}
	public void setUrl_pdf(String url_pdf) {
		this.url_pdf = url_pdf;
	}
	
	
    
    
    
    


}
