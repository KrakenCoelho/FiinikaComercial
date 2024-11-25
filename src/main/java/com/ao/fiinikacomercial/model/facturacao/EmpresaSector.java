package com.ao.fiinikacomercial.model.facturacao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;



@Entity
public class EmpresaSector {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    
    @ManyToOne
  	@JoinColumn(name = "empresa_idfk")
  	private Empresa empresa;
    
    @ManyToOne
  	@JoinColumn(name = "sector_idfk")
  	private Sectores sectorIdfk;

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Sectores getSectorIdfk() {
		return sectorIdfk;
	}

	public void setSectorIdfk(Sectores sectorIdfk) {
		this.sectorIdfk = sectorIdfk;
	}
    
    
   
    
    
    

}
