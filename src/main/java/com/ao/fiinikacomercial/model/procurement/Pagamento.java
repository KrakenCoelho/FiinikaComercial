package com.ao.fiinikacomercial.model.procurement;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Pagamento {

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id_pag;
	
	 @Column
	 private Long empresa_id;
	 
	 @Column
	 private Long empresa_que_paga;
	 
	 @Column
	 private Long pedido_id;
	 
	 @Column
	 private String produtos_ids;
	 
	 @Column 
	 private Long proposta_id;
	 
	 @Column
	 private String entidade;
	 
	 @Column
	 private String referencia;
	 
	 @Column
	 private String tipo;
	 
	 @Column
	 private String estatuto;
	 
	 @Column
	 private String valor;
	 
	 @Column
	 private String valor_sem_taxa;
	 
	 @Column
	 private String taxa_servico;
	 
 	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "YYYY-MM-dd")
	private Date data_pag;
	 
 	@Column
	 private String estado_pagamento;
	 
	 
	public Pagamento() {
		super();
	}

	   

	public Pagamento(Long id_pag, Long empresa_id, Long empresa_que_paga, Long pedido_id, String produtos_ids,
			Long proposta_id, String entidade, String referencia, String tipo, String estatuto, String valor,
			String valor_sem_taxa, String estado_pagamento,String taxa_servico, Date data_pag) {
		super();
		this.id_pag = id_pag;
		this.empresa_id = empresa_id;
		this.empresa_que_paga = empresa_que_paga;
		this.pedido_id = pedido_id;
		this.produtos_ids = produtos_ids;
		this.proposta_id = proposta_id;
		this.entidade = entidade;
		this.referencia = referencia;
		this.tipo = tipo;
		this.estatuto = estatuto;
		this.valor = valor;
		this.valor_sem_taxa = valor_sem_taxa;
		this.taxa_servico = taxa_servico;
		this.data_pag = data_pag;
		this.estado_pagamento = estado_pagamento;
	}

	public Long getId_pag() {
		return id_pag;
	}



 
	public void setId_pag(Long id_pag) {
		this.id_pag = id_pag;
	}





	public Long getEmpresa_id() {
		return empresa_id;
	}





	public void setEmpresa_id(Long empresa_id) {
		this.empresa_id = empresa_id;
	}





	public Long getPedido_id() {
		return pedido_id;
	}





	public void setPedido_id(Long pedido_id) {
		this.pedido_id = pedido_id;
	}




 
	





	public String getProdutos_ids() {
		return produtos_ids;
	}







	public void setProdutos_ids(String produtos_ids) {
		this.produtos_ids = produtos_ids;
	}







	public Long getProposta_id() {
		return proposta_id;
	}





	public void setProposta_id(Long proposta_id) {
		this.proposta_id = proposta_id;
	}





	public String getEntidade() {
		return entidade;
	}





	public void setEntidade(String entidade) {
		this.entidade = entidade;
	}





	public String getReferencia() {
		return referencia;
	}





	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}





	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public String getEstatuto() {
		return estatuto;
	}

  
	public void setEstatuto(String estatuto) {
		this.estatuto = estatuto;
	}

	public Date getData_pag() {
		return data_pag;
	}


	public void setData_pag(Date data_pag) {
		this.data_pag = data_pag;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}



	public Long getEmpresa_que_paga() {
		return empresa_que_paga;
	}



	public void setEmpresa_que_paga(Long empresa_que_paga) {
		this.empresa_que_paga = empresa_que_paga;
	}



	public String getValor_sem_taxa() {
		return valor_sem_taxa;
	}



	public void setValor_sem_taxa(String valor_sem_taxa) {
		this.valor_sem_taxa = valor_sem_taxa;
	}



	public String getTaxa_servico() {
		return taxa_servico;
	}



	public void setTaxa_servico(String taxa_servico) {
		this.taxa_servico = taxa_servico;
	}



	public String getEstado_pagamento() {
		return estado_pagamento;
	}



	public void setEstado_pagamento(String estado_pagamento) {
		this.estado_pagamento = estado_pagamento;
	}
	
	
	
	

}
