package com.ao.fiinikacomercial.model.facturacao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Recibo {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_recibo;
	private String num_factura;
	private String percentagem;
	private long id_contafk;
	private String valor_pago;
	private String foto_recibo;
	private String estado_recibo;
	private String data_recibo;
	private long id_facturafk;
	private String data_hora_recibo;
	private String data_pagamento;
	private long increment_rec;
	private String codigo_recibo;
	private String taxa_recibo;
	private String total_recibo;
	private String vendedor_recibo;
	private String retencao;
	private String code_recibo;
	private long id_vendedorfk;
	private long id_empresa_fkR;
	
	
	
	public long getId_facturafk() {
		return id_facturafk;
	}
	public void setId_facturafk(long id_facturafk) {
		this.id_facturafk = id_facturafk;
	}
	public String getValor_pago() {
		return valor_pago;
	}
	public void setValor_pago(String valor_pago) {
		this.valor_pago = valor_pago;
	}
	public String getNum_factura() {
		return num_factura;
	}
	public void setNum_factura(String num_factura) {
		this.num_factura = num_factura;
	}
	public String getData_recibo() {
		return data_recibo;
	}
	public void setData_recibo(String data_recibo) {
		this.data_recibo = data_recibo;
	}
	public long getId_recibo() {
		return id_recibo;
	}
	public void setId_recibo(long id_recibo) {
		this.id_recibo = id_recibo;
	}
	public String getPercentagem() {
		return percentagem;
	}
	public void setPercentagem(String percentagem) {
		this.percentagem = percentagem;
	}
	public long getId_contafk() {
		return id_contafk;
	}
	public void setId_contafk(long id_contafk) {
		this.id_contafk = id_contafk;
	}
	public String getFoto_recibo() {
		return foto_recibo;
	}
	public void setFoto_recibo(String foto_recibo) {
		this.foto_recibo = foto_recibo;
	}
	public String getEstado_recibo() {
		return estado_recibo;
	}
	public void setEstado_recibo(String estado_recibo) {
		this.estado_recibo = estado_recibo;
	}

	public String getData_hora_recibo() {
		return data_hora_recibo;
	}
	public void setData_hora_recibo(String data_hora_recibo) {
		this.data_hora_recibo = data_hora_recibo;
	}
	public String getData_pagamento() {
		return data_pagamento;
	}
	public void setData_pagamento(String data_pagamento) {
		this.data_pagamento = data_pagamento;
	}
	public long getIncrement_rec() {
		return increment_rec;
	}
	public void setIncrement_rec(long increment_rec) {
		this.increment_rec = increment_rec;
	}
	public String getCodigo_recibo() {
		return codigo_recibo;
	}
	public void setCodigo_recibo(String codigo_recibo) {
		this.codigo_recibo = codigo_recibo;
	}
	
	public String getTaxa_recibo() {
		return taxa_recibo;
	}
	public void setTaxa_recibo(String taxa_recibo) {
		this.taxa_recibo = taxa_recibo;
	}
	public String getTotal_recibo() {
		return total_recibo;
	}
	public void setTotal_recibo(String total_recibo) {
		this.total_recibo = total_recibo;
	}
	public String getVendedor_recibo() {
		return vendedor_recibo;
	}
	public void setVendedor_recibo(String vendedor_recibo) {
		this.vendedor_recibo = vendedor_recibo;
	}
	public String getRetencao() {
		return retencao;
	}
	public void setRetencao(String retencao) {
		this.retencao = retencao;
	}
	public String getCode_recibo() {
		return code_recibo;
	}
	public void setCode_recibo(String code_recibo) {
		this.code_recibo = code_recibo;
	}
	public long getId_vendedorfk() {
		return id_vendedorfk;
	}
	public void setId_vendedorfk(long id_vendedorfk) {
		this.id_vendedorfk = id_vendedorfk;
	}
	public long getId_empresa_fkR() {
		return id_empresa_fkR;
	}
	public void setId_empresa_fkR(long id_empresa_fkR) {
		this.id_empresa_fkR = id_empresa_fkR;
	}
	
	
	
	
	

}
