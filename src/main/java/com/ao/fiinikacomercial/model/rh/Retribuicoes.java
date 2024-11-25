package com.ao.fiinikacomercial.model.rh;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Retribuicoes {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column
	private String name;
	@Column
	private String incidence;
	@Column
	private int creator_id;
	@Column
	private String creator_name;
	@Column
	private String irt;
	@Column
	private String inss;
	@Column
	private int percentage;
	@Column
	private String set_value;
	@Column
	private String status;
	@Column
	private String creation_date;
	@Column(columnDefinition = "TEXT")
	private String targets;
	@Column(nullable = false, columnDefinition = "int(11) default 0")
	private int vencimento_id = 0;
	@Column(nullable = false, columnDefinition = "int(11) default 0")
	private long empresa_id;
	
	
	public Retribuicoes() {

	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getIncidence() {
		return incidence;
	}


	public void setIncidence(String incidence) {
		this.incidence = incidence;
	}


	public int getCreator_id() {
		return creator_id;
	}


	public void setCreator_id(int creator_id) {
		this.creator_id = creator_id;
	}


	public String getCreator_name() {
		return creator_name;
	}


	public void setCreator_name(String creator_name) {
		this.creator_name = creator_name;
	}


	public String getIrt() {
		return irt;
	}


	public void setIrt(String irt) {
		this.irt = irt;
	}


	public String getInss() {
		return inss;
	}


	public void setInss(String inss) {
		this.inss = inss;
	}


	public int getPercentage() {
		return percentage;
	}


	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}


	public String getSet_value() {
		return set_value;
	}


	public void setSet_value(String set_value) {
		this.set_value = set_value;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getCreation_date() {
		return creation_date;
	}


	public void setCreation_date(String creation_date) {
		this.creation_date = creation_date;
	}


	public String getTargets() {
		return targets;
	}


	public void setTargets(String targets) {
		this.targets = targets;
	}


	public int getVencimento_id() {
		return vencimento_id;
	}


	public void setVencimento_id(int vencimento_id) {
		this.vencimento_id = vencimento_id;
	}


	public long getEmpresa_id() {
		return empresa_id;
	}


	public void setEmpresa_id(long empresa_id) {
		this.empresa_id = empresa_id;
	}


	


	
	
}
