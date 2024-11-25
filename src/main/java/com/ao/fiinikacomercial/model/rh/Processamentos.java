package com.ao.fiinikacomercial.model.rh;

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
public class Processamentos {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column
	private int admin_id;
	@Column
	private String admin_name;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date start_date;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date end_date;
	@Column
	private String funcionarios_ids;
	@Column
	private String retributions;
	@Column
	private String deductions;
	@Column
	private String total_processed;
	@Column
	private String total_irt;
	@Column
	private String total_gross_salary;
	@Column
	private String total_net_salary;
	@Column
	private String total_social_security;
	@Column
	private String total_social_security_company;
	@Column
	private String total_discounts;
	@Column
	private String total_subsidies;
	@Column
	private String total_salary_kwanza;
	@Column
	private String total_salary_dollar;
	@Column
	private String total_salary_euro;
	@Column
	private String month;
	@Column
	private int year;
	@Column
	private String status;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date creation_date;
	@Column(nullable = false, columnDefinition = "int(11) default 0")
	private long empresa_id;
	
	
	
	public Processamentos() {
		
	}
	

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getAdmin_id() {
		return admin_id;
	}


	public void setAdmin_id(int admin_id) {
		this.admin_id = admin_id;
	}


	public String getAdmin_name() {
		return admin_name;
	}


	public void setAdmin_name(String admin_name) {
		this.admin_name = admin_name;
	}


	public Date getStart_date() {
		return start_date;
	}


	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}


	public Date getEnd_date() {
		return end_date;
	}


	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}


	public String getFuncionarios_ids() {
		return funcionarios_ids;
	}


	public void setFuncionarios_ids(String funcionarios_ids) {
		this.funcionarios_ids = funcionarios_ids;
	}


	public String getRetributions() {
		return retributions;
	}


	public void setRetributions(String retributions) {
		this.retributions = retributions;
	}


	public String getDeductions() {
		return deductions;
	}


	public void setDeductions(String deductions) {
		this.deductions = deductions;
	}


	public String getTotal_processed() {
		return total_processed;
	}


	public void setTotal_processed(String total_processed) {
		this.total_processed = total_processed;
	}


	public String getTotal_irt() {
		return total_irt;
	}


	public void setTotal_irt(String total_irt) {
		this.total_irt = total_irt;
	}


	public String getTotal_gross_salary() {
		return total_gross_salary;
	}


	public void setTotal_gross_salary(String total_gross_salary) {
		this.total_gross_salary = total_gross_salary;
	}


	public String getTotal_net_salary() {
		return total_net_salary;
	}


	public void setTotal_net_salary(String total_net_salary) {
		this.total_net_salary = total_net_salary;
	}


	public String getTotal_social_security() {
		return total_social_security;
	}


	public void setTotal_social_security(String total_social_security) {
		this.total_social_security = total_social_security;
	}


	public String getTotal_social_security_company() {
		return total_social_security_company;
	}


	public void setTotal_social_security_company(String total_social_security_company) {
		this.total_social_security_company = total_social_security_company;
	}


	public String getTotal_discounts() {
		return total_discounts;
	}


	public void setTotal_discounts(String total_discounts) {
		this.total_discounts = total_discounts;
	}


	public String getTotal_subsidies() {
		return total_subsidies;
	}


	public void setTotal_subsidies(String total_subsidies) {
		this.total_subsidies = total_subsidies;
	}


	public String getTotal_salary_kwanza() {
		return total_salary_kwanza;
	}


	public void setTotal_salary_kwanza(String total_salary_kwanza) {
		this.total_salary_kwanza = total_salary_kwanza;
	}


	public String getTotal_salary_dollar() {
		return total_salary_dollar;
	}


	public void setTotal_salary_dollar(String total_salary_dollar) {
		this.total_salary_dollar = total_salary_dollar;
	}


	public String getTotal_salary_euro() {
		return total_salary_euro;
	}


	public void setTotal_salary_euro(String total_salary_euro) {
		this.total_salary_euro = total_salary_euro;
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


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Date getCreation_date() {
		return creation_date;
	}


	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}


	public long getEmpresa_id() {
		return empresa_id;
	}


	public void setEmpresa_id(long empresa_id) {
		this.empresa_id = empresa_id;
	}
	
	


	


	
	
	
	
	

}
