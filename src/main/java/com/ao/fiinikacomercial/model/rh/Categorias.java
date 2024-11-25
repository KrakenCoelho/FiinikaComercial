package com.ao.fiinikacomercial.model.rh;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="CategoriaF")
public class Categorias {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column
	private String name;
	@Column
	private String base_salary;
	@Column
	private String retribution;
	@Column
	private String deduction;
	@Column
	private int department;
	@Column
	private String department_name;
	@Column
	private String creation_date;
	@Column(nullable = false, columnDefinition = "int(11) default 0")
	private long empresa_id;
	
	
	public Categorias() {
		
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


	public String getBase_salary() {
		return base_salary;
	}


	public void setBase_salary(String base_salary) {
		this.base_salary = base_salary;
	}


	public String getRetribution() {
		return retribution;
	}


	public void setRetribution(String retribution) {
		this.retribution = retribution;
	}


	public String getDeduction() {
		return deduction;
	}


	public void setDeduction(String deduction) {
		this.deduction = deduction;
	}


	public int getDepartment() {
		return department;
	}


	public void setDepartment(int department) {
		this.department = department;
	}


	public String getDepartment_name() {
		return department_name;
	}


	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}


	public String getCreation_date() {
		return creation_date;
	}


	public void setCreation_date(String creation_date) {
		this.creation_date = creation_date;
	}


	public long getEmpresa_id() {
		return empresa_id;
	}


	public void setEmpresa_id(long empresa_id) {
		this.empresa_id = empresa_id;
	}
	
	
	
	

}
