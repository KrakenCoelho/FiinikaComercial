package com.ao.fiinikacomercial.model.procurement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class PedidoSector {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long idPS;
	
	@Column
	private Long sectorIdFK;
	
	@Column
	private Long pedidoIdFK;

	public Long getIdPS() {
		return idPS;
	}

	public void setIdPS(Long idPS) {
		this.idPS = idPS;
	}

	public Long getSectorIdFK() {
		return sectorIdFK;
	}

	public void setSectorIdFK(Long sectorIdFK) {
		this.sectorIdFK = sectorIdFK;
	}

	public Long getPedidoIdFK() {
		return pedidoIdFK;
	}

	public void setPedidoIdFK(Long pedidoIdFK) {
		this.pedidoIdFK = pedidoIdFK;
	}
	
	
	
	
}
 