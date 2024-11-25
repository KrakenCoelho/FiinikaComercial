package com.ao.fiinikacomercial.model.facturacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Item {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id_item;
	private long id_categoriafk;
    private long id_produtofk;
    private int qtd;
    private String preco;
    private String subtotal;
    private long id_facturafk;
    private String ref_cat;
    private String taxa_prod_item;
    @Column(columnDefinition = "varchar(255) default '---'")
    private String codigo_ise;
    private String descricao_prod;
    private String motivo_ise;
    private String tipo_item;
    private long idEmpresafork;
    
    
	public String getRef_cat() {
		return ref_cat;
	}
	public void setRef_cat(String ref_cat) {
		this.ref_cat = ref_cat;
	}
	public long getId_item() {
		return id_item;
	}
	public void setId_item(long id_item) {
		this.id_item = id_item;
	}
	public long getId_categoriafk() {
		return id_categoriafk;
	}
	public void setId_categoriafk(long id_categoriafk) {
		this.id_categoriafk = id_categoriafk;
	}
	public long getId_produtofk() {
		return id_produtofk;
	}
	public void setId_produtofk(long id_produtofk) {
		this.id_produtofk = id_produtofk;
	}
	public int getQtd() {
		return qtd;
	}
	public void setQtd(int qtd) {
		this.qtd = qtd;
	}

	public String getPreco() {
		return preco;
	}
	public void setPreco(String preco) {
		this.preco = preco;
	}
	public String getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}
	public long getId_facturafk() {
		return id_facturafk;
	}
	public void setId_facturafk(long id_facturafk) {
		this.id_facturafk = id_facturafk;
	}
	public String getTaxa_prod_item() {
		return taxa_prod_item;
	}
	public void setTaxa_prod_item(String taxa_prod_item) {
		this.taxa_prod_item = taxa_prod_item;
	}
	public String getCodigo_ise() {
		return codigo_ise;
	}
	public void setCodigo_ise(String codigo_ise) {
		this.codigo_ise = codigo_ise;
	}
	public String getDescricao_prod() {
		return descricao_prod;
	}
	public void setDescricao_prod(String descricao_prod) {
		this.descricao_prod = descricao_prod;
	}
	public String getMotivo_ise() {
		return motivo_ise;
	}
	public void setMotivo_ise(String motivo_ise) {
		this.motivo_ise = motivo_ise;
	}
	public String getTipo_item() {
		return tipo_item;
	}
	public void setTipo_item(String tipo_item) {
		this.tipo_item = tipo_item;
	}
	public long getIdEmpresafork() {
		return idEmpresafork;
	}
	public void setIdEmpresafork(long idEmpresafork) {
		this.idEmpresafork = idEmpresafork;
	}
	
    

}
