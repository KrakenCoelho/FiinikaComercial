package com.ao.fiinikacomercial.model.facturacao;


	/*
	 * To change this license header, choose License Headers in Project Properties.
	 * To change this template file, choose Tools | Templates
	 * and open the template in the editor.
	 */

	/**
	 *
	 * @author Heitor
	 */
	import javax.persistence.Column;
	import javax.persistence.Entity;
	import javax.persistence.GeneratedValue;
	import javax.persistence.GenerationType;
	import javax.persistence.Id;

	@Entity
	public class Empresa
	{
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private long idEmpresa;

	    @Column(nullable = false)
	    private String nomeEmpresa;
	    
	    private String nif;
	    private String endereco;
	    private String telemovel;
	    private String telemovel2;
	    private String email;
	    private String website;
	    private String foto_logotipo;
	    private String regime;
	    private int control_nif;
	    private String cambioDollar;
	    private String cambioEuro;
	    
		public String getCambioDollar() {
			return cambioDollar;
		}
		public void setCambioDollar(String cambioDollar) {
			this.cambioDollar = cambioDollar;
		}
		public String getCambioEuro() {
			return cambioEuro;
		}
		public void setCambioEuro(String cambioEuro) {
			this.cambioEuro = cambioEuro;
		}
		public long getIdEmpresa() {
			return idEmpresa;
		}
		public void setIdEmpresa(long idEmpresa) {
			this.idEmpresa = idEmpresa;
		}
		public String getNomeEmpresa() {
			return nomeEmpresa;
		}
		public void setNomeEmpresa(String nomeEmpresa) {
			this.nomeEmpresa = nomeEmpresa;
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
		public String getTelemovel() {
			return telemovel;
		}
		public void setTelemovel(String telemovel) {
			this.telemovel = telemovel;
		}
		public String getTelemovel2() {
			return telemovel2;
		}
		public void setTelemovel2(String telemovel2) {
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
		public String getRegime() {
			return regime;
		}
		public void setRegime(String regime) {
			this.regime = regime;
		}
		public int getControl_nif() {
			return control_nif;
		}
		public void setControl_nif(int control_nif) {
			this.control_nif = control_nif;
		}
	    
	    
	   

		
	}
