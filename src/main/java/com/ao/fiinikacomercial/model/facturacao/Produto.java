package com.ao.fiinikacomercial.model.facturacao;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Produto {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idProduto;

    @Column(nullable = false)
    private String nomeProduto;
    private String preco_produto;
    private String foto_produto;
    private long id_categoriafk;    
    private long id_isencaofk;
    private String taxa_prod;
    @Column(columnDefinition = "TEXT")
    private String descricao_p;
    private String tipo_produto;
    private long idEmpresaFkp;
    private int qtdStock;
    private String codBarra;
    private String referencia;
    
    
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public String getCodBarra() {
		return codBarra;
	}
	public void setCodBarra(String codBarra) {
		this.codBarra = codBarra;
	}
	public long getIdProduto() {
		return idProduto;
	}
	public void setIdProduto(long idProduto) {
		this.idProduto = idProduto;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	public long getIdEmpresaFkp() {
		return idEmpresaFkp;
	}
	public void setIdEmpresaFkp(long idEmpresaFkp) {
		this.idEmpresaFkp = idEmpresaFkp;
	}
	public String getPreco_produto() {
		return preco_produto;
	}
	public void setPreco_produto(String preco_produto) {
		this.preco_produto = preco_produto;
	}
	public String getFoto_produto() {
		return foto_produto;
	}
	public void setFoto_produto(String foto_produto) {
		this.foto_produto = foto_produto;
	}
	public long getId_categoriafk() {
		return id_categoriafk;
	}
	public void setId_categoriafk(long id_categoriafk) {
		this.id_categoriafk = id_categoriafk;
	}
	public long getId_isencaofk() {
		return id_isencaofk;
	}
	public void setId_isencaofk(long id_isencaofk) {
		this.id_isencaofk = id_isencaofk;
	}
	public String getTaxa_prod() {
		return taxa_prod;
	}
	public void setTaxa_prod(String taxa_prod) {
		this.taxa_prod = taxa_prod;
	}
	public String getDescricao_p() {
		return descricao_p;
	}
	public void setDescricao_p(String descricao_p) {
		this.descricao_p = descricao_p;
	}
	public String getTipo_produto() {
		return tipo_produto;
	}
	public void setTipo_produto(String tipo_produto) {
		this.tipo_produto = tipo_produto;
	}
	public int getQtdStock() {
		return qtdStock;
	}
	public void setQtdStock(int qtdStock) {
		this.qtdStock = qtdStock;
	}

	
	
    
}

