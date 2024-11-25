package com.ao.fiinikacomercial.model.facturacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Conta {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idConta;

    @Column(nullable = false)
    private String banco;
    private String nomeConta;
    private String iban;
    private long idEmpresafk;
    private String numero_conta;
    private String foto_logotipo;
    private String titular;
    private String gestor;
    private String telemovel;
    private String email;
    private String dependencia;
    private String endereco;
    private int padrao;
    private String sigla;
    

	public long getIdConta() {
		return idConta;
	}
	public void setIdConta(long idConta) {
		this.idConta = idConta;
	}
	public String getNomeConta() {
		return nomeConta;
	}
	public void setNomeConta(String nomeConta) {
		this.nomeConta = nomeConta;
	}
	public long getIdEmpresafk() {
		return idEmpresafk;
	}
	public void setIdEmpresafk(long idEmpresafk) {
		this.idEmpresafk = idEmpresafk;
	}
	public String getBanco() {
		return banco;
	}
	public void setBanco(String banco) {
		this.banco = banco;
	}
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getNumero_conta() {
		return numero_conta;
	}
	public void setNumero_conta(String numero_conta) {
		this.numero_conta = numero_conta;
	}
	public String getFoto_logotipo() {
		return foto_logotipo;
	}
	public void setFoto_logotipo(String foto_logotipo) {
		this.foto_logotipo = foto_logotipo;
	}
	public String getTitular() {
		return titular;
	}
	public void setTitular(String titular) {
		this.titular = titular;
	}
	public String getGestor() {
		return gestor;
	}
	public void setGestor(String gestor) {
		this.gestor = gestor;
	}
	public String getTelemovel() {
		return telemovel;
	}
	public void setTelemovel(String telemovel) {
		this.telemovel = telemovel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDependencia() {
		return dependencia;
	}
	public void setDependencia(String dependencia) {
		this.dependencia = dependencia;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public int getPadrao() {
		return padrao;
	}
	public void setPadrao(int padrao) {
		this.padrao = padrao;
	}
	public String getSigla() {
		return sigla;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
   
}
