package com.ao.fiinikacomercial.model.rh;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Ausencias {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column
	private int registered_by;
	@Column
	private String registerer_name;
	@Column
	private String employee_name;
	@Column
	private int employee_id;
	@Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
	@Column
	private String justification;
	@Column
	private String document;
	@Column
	private String status;
	@Column
	private String month;
	@Column
	private int year;
	@Column
	private String employee_photo;
	@Column
	private String absence_fee;
	@Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date period;
	@Column(nullable = false, columnDefinition = "int(11) default 0")
	private long empresa_id;
	
	
	
	public Ausencias() {
		
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public int getRegistered_by() {
		return registered_by;
	}



	public void setRegistered_by(int registered_by) {
		this.registered_by = registered_by;
	}



	public String getRegisterer_name() {
		return registerer_name;
	}



	public void setRegisterer_name(String registerer_name) {
		this.registerer_name = registerer_name;
	}



	public String getEmployee_name() {
		return employee_name;
	}



	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}



	public int getEmployee_id() {
		return employee_id;
	}



	public void setEmployee_id(int employee_id) {
		this.employee_id = employee_id;
	}



	public Date getDate() {
		return date;
	}



	public void setDate(Date date) {
		this.date = date;
	}



	public String getJustification() {
		return justification;
	}



	public void setJustification(String justification) {
		this.justification = justification;
	}



	public String getDocument() {
		return document;
	}



	public void setDocument(String document) {
		this.document = document;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getMonth() {
		return month;
	}



	public void setMonth(String month) {
		this.month = month;
	}



	public int getYear() {
		return year;
	}



	public void setYear(int year) {
		this.year = year;
	}



	public String getEmployee_photo() {
		return employee_photo;
	}



	public void setEmployee_photo(String employee_photo) {
		this.employee_photo = employee_photo;
	}



	public String getAbsence_fee() {
		return absence_fee;
	}



	public void setAbsence_fee(String absence_fee) {
		this.absence_fee = absence_fee;
	}



	public Date getPeriod() {
		return period;
	}



	public void setPeriod(Date period) {
		this.period = period;
	}



	public long getEmpresa_id() {
		return empresa_id;
	}



	public void setEmpresa_id(long empresa_id) {
		this.empresa_id = empresa_id;
	}

	
	



	
	
	
	
	
	

}
