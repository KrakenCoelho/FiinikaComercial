package com.ao.fiinikacomercial.model.facturacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Categoria {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCategoria;

    @Column(nullable = false)
    private String nomeCategoria;
    private String referencia_categoria;
    private String foto_categoria;
    private long idEmpresafk;
    
    
	
	public long getIdCategoria() {
		return idCategoria;
	}
	public void setIdCategoria(long idCategoria) {
		this.idCategoria = idCategoria;
	}
	public String getNomeCategoria() {
		return nomeCategoria;
	}
	public void setNomeCategoria(String nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}
	public long getIdEmpresafk() {
		return idEmpresafk;
	}
	public void setIdEmpresafk(long idEmpresafk) {
		this.idEmpresafk = idEmpresafk;
	}
	public String getReferencia_categoria() {
		return referencia_categoria;
	}
	public void setReferencia_categoria(String referencia_categoria) {
		this.referencia_categoria = referencia_categoria;
	}
	public String getFoto_categoria() {
		return foto_categoria;
	}
	public void setFoto_categoria(String foto_categoria) {
		this.foto_categoria = foto_categoria;
	}
	
	

}
