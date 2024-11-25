package com.ao.fiinikacomercial.model.facturacao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ItemNotas {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id_itemNota;
	
	private long id_categoriafk;
    private long id_produtofk;
    private int qtd;
    private float preco;
    private float subtotal;
    private long id_notafk;
    private String ref_cat;
    private String taxa_prod_nota;
    private String codigo_ise;
    private String descricao_prod;
    private String motivo_ise;
    private String tipo_item_nota;
    private long idEmpresafork;
    
	public long getId_itemNota() {
		return id_itemNota;
	}
	public void setId_itemNota(long id_itemNota) {
		this.id_itemNota = id_itemNota;
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
	public float getPreco() {
		return preco;
	}
	public void setPreco(float preco) {
		this.preco = preco;
	}
	public float getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(float subtotal) {
		this.subtotal = subtotal;
	}
	public long getId_notafk() {
		return id_notafk;
	}
	public void setId_notafk(long id_notafk) {
		this.id_notafk = id_notafk;
	}
	public String getRef_cat() {
		return ref_cat;
	}
	public void setRef_cat(String ref_cat) {
		this.ref_cat = ref_cat;
	}
	public String getTaxa_prod_nota() {
		return taxa_prod_nota;
	}
	public void setTaxa_prod_nota(String taxa_prod_nota) {
		this.taxa_prod_nota = taxa_prod_nota;
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
	public String getTipo_item_nota() {
		return tipo_item_nota;
	}
	public void setTipo_item_nota(String tipo_item_nota) {
		this.tipo_item_nota = tipo_item_nota;
	}
	public long getIdEmpresafork() {
		return idEmpresafork;
	}
	public void setIdEmpresafork(long idEmpresafork) {
		this.idEmpresafork = idEmpresafork;
	}
    
    
    
    

}
