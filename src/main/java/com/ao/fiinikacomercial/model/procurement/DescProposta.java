package com.ao.fiinikacomercial.model.procurement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class DescProposta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long iddp;
	
	
	@Column
	private Long id_categoria;
	@Column
	private Long id_produto;
	@Column
	private Long concurso_id;
	@Column
	private Long empresa_prop_id;
	@Column
	private String preco;
	@Column
	private String taxa_prod;
	@Column
	private String ref_cat;
	@Column
	private String qtd;
	@Column
	private String estadodp;
	@Column
	private String tipo_prod;
	
	@Column
	private String selecionado;
	@Column
	private String subtotal;
	
	public Long getConcurso_id() {
		return concurso_id;
	}
	public void setConcurso_id(Long concurso_id) {
		this.concurso_id = concurso_id;
	}
	public Long getEmpresa_prop_id() {
		return empresa_prop_id;
	}
	public void setEmpresa_prop_id(Long empresa_prop_id) {
		this.empresa_prop_id = empresa_prop_id;
	}

	
	public Long getIddp() {
		return iddp;
	}
	public void setIddp(Long iddp) {
		this.iddp = iddp;
	}
	public Long getId_categoria() {
		return id_categoria;
	}
	public void setId_categoria(Long id_categoria) {
		this.id_categoria = id_categoria;
	}
	public Long getId_produto() {
		return id_produto;
	}
	public void setId_produto(Long id_produto) {
		this.id_produto = id_produto;
	}
	public String getPreco() {
		return preco;
	}
	public void setPreco(String preco) {
		this.preco = preco;
	}
	public String getTaxa_prod() {
		return taxa_prod;
	}
	public void setTaxa_prod(String taxa_prod) {
		this.taxa_prod = taxa_prod;
	}
	public String getRef_cat() {
		return ref_cat;
	}
	public void setRef_cat(String ref_cat) {
		this.ref_cat = ref_cat;
	}
	public String getQtd() {
		return qtd;
	}
	public void setQtd(String qtd) {
		this.qtd = qtd;
	}
	public String getTipo_prod() {
		return tipo_prod;
	}
	public void setTipo_prod(String tipo_prod) {
		this.tipo_prod = tipo_prod;
	}
	public String getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}
	public String getEstadodp() {
		return estadodp;
	}
	public void setEstadodp(String estadodp) {
		this.estadodp = estadodp;
	}
	public String getSelecionado() {
		return selecionado;
	}
	public void setSelecionado(String selecionado) {
		this.selecionado = selecionado;
	}
	
	
	
	
	
	
}
