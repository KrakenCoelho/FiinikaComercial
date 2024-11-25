package com.ao.fiinikacomercial.model.facturacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Cliente {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private long idCliente;

	    @Column(nullable = false)
	    private String nomeCliente;
	    
	    private String nif;
	    private String endereco;
	    private Integer telemovel;
	    private Integer telemovel2;
	    private String email;
	    private String website;
	    private String foto_logotipo;
	    private long idEmpresafk;
	    private String tipo_cliente;
	    private String data_cadastro;
	    private String criado_por;
	    private long idCriadorfk;
		public long getIdCliente() {
			return idCliente;
		}
		public void setIdCliente(long idCliente) {
			this.idCliente = idCliente;
		}
		public String getNomeCliente() {
			return nomeCliente;
		}
		public void setNomeCliente(String nomeCliente) {
			this.nomeCliente = nomeCliente;
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
		public String getTipo_cliente() {
			return tipo_cliente;
		}
		public void setTipo_cliente(String tipo_cliente) {
			this.tipo_cliente = tipo_cliente;
		}
		public String getData_cadastro() {
			return data_cadastro;
		}
		public void setData_cadastro(String data_cadastro) {
			this.data_cadastro = data_cadastro;
		}
		public String getCriado_por() {
			return criado_por;
		}
		public void setCriado_por(String criado_por) {
			this.criado_por = criado_por;
		}
		public long getIdCriadorfk() {
			return idCriadorfk;
		}
		public void setIdCriadorfk(long idCriadorfk) {
			this.idCriadorfk = idCriadorfk;
		}
	    
		
	    
	    

}
