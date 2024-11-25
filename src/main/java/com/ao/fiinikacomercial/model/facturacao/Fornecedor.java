package com.ao.fiinikacomercial.model.facturacao;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Fornecedor {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idFornecedor;
	
	@Column(nullable = false)
    private String nome_fornecedor;
    
    private String nif;
    private String endereco;
    private Integer telemovel;
    private Integer telemovel2;
    private String email;
    private String website;
    private String foto_logotipo;
    private long idEmpresafk;
    private String data_cadastro;
	public long getIdFornecedor() {
		return idFornecedor;
	}
	public void setIdFornecedor(long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}
	public String getNome_fornecedor() {
		return nome_fornecedor;
	}
	public void setNome_fornecedor(String nome_fornecedor) {
		this.nome_fornecedor = nome_fornecedor;
	}
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public Integer getTelemovel() {
		return telemovel;
	}
	public void setTelemovel(Integer telemovel) {
		this.telemovel = telemovel;
	}
	public Integer getTelemovel2() {
		return telemovel2;
	}
	public void setTelemovel2(Integer telemovel2) {
		this.telemovel2 = telemovel2;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getFoto_logotipo() {
		return foto_logotipo;
	}
	public void setFoto_logotipo(String foto_logotipo) {
		this.foto_logotipo = foto_logotipo;
	}
	public long getIdEmpresafk() {
		return idEmpresafk;
	}
	public void setIdEmpresafk(long idEmpresafk) {
		this.idEmpresafk = idEmpresafk;
	}
	public String getData_cadastro() {
		return data_cadastro;
	}
	public void setData_cadastro(String data_cadastro) {
		this.data_cadastro = data_cadastro;
	}

	

}

