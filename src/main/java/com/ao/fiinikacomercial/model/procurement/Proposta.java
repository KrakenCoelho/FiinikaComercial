package com.ao.fiinikacomercial.model.procurement;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Proposta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idProposta;
	
	@Column
	private Long empresaPropostaId;
	
	@Column
	private Long idconcu;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "YYYY-MM-dd")
	private Date dataProposta;
	
	@Column
	private String estadoProposta;

	public Long getIdProposta() {
		return idProposta;
	}

	public void setIdProposta(Long idProposta) { 
		this.idProposta = idProposta;
	}

	public Long getEmpresaPropostaId() {
		return empresaPropostaId;
	}

	public void setEmpresaPropostaId(Long empresaPropostaId) {
		this.empresaPropostaId = empresaPropostaId;
	}

	public Long getIdconcu() {
		return idconcu;
	}

	public void setIdconcu(Long idconcu) {
		this.idconcu = idconcu;
	}

	public Date getDataProposta() {
		return dataProposta;
	}

	

	public void setDataProposta(Date dataProposta) {
		this.dataProposta = dataProposta;
	}

	public String getEstadoProposta() {
		return estadoProposta;
	}

	public void setEstadoProposta(String estadoProposta) {
		this.estadoProposta = estadoProposta;
	}
	
	@Override
	public String toString() {
		return "Proposta [idProposta=" + idProposta + ", empresaPropostaId=" + empresaPropostaId + ", idconcurso="
				+ idconcu + ", dataProposta=" + dataProposta + ", estadoProposta=" + estadoProposta + "]";
	}
	
}
