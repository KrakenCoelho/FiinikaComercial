package com.ao.fiinikacomercial.model.facturacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Sectores {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idSector;
	
	@Column
	private String sector;
	 
	
	

	public Long getIdSector() {
		return idSector;
	}

	public void setIdSector(Long idSector) {
		this.idSector = idSector;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}
	
	
	
}
