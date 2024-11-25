package com.ao.fiinikacomercial.model.rh;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Vencimentos {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column
	private int admin_id;
	@Column
	private String admin_name;
	@Column(nullable = false, columnDefinition = "int(11) default 0")
	private int funcionario_id = 0;
	@Column
	private String user_type;
	@Column
	private String name;
	@Column(nullable = false, columnDefinition = "int(11) default 0")
	private int processamento_id = 0;
	@Column
	private String gender;
	@Column
	private String nif;
	@Column
	private String iban; 
	@Column
	private String inss;
	@Column
	private String inss_company;
	@Column
	private String irt_fee;
	@Column
	private String address;
	@Column
	private String email;
	@Column
	private String phone;
	@Column
	private String alt_phone;
	@Column(nullable = false, columnDefinition = "int(11) default 0")
	private int category = 0;
	@Column
	private String type_of_contract;
	@Column
	private String id_document;
	@Column
	private String admission_date;
	@Column
	private String category_name;
	@Column(nullable = false, columnDefinition = "int(11) default 0")
	private int dependants = 0;
	@Column(nullable = false, columnDefinition = "int(11) default 0")
	private int hours_per_day =0;
	@Column(nullable = false, columnDefinition = "int(11) default 0")
	private int days_per_week = 0;
	@Column
	private String cost_per_hour;
	@Column(nullable = false, columnDefinition = "text default '0.00KZ'")
	private String base_salary = "0.00KZ";
	@Column
	private String retributions;
	@Column
	private String deductions;
	@Column(nullable = false, columnDefinition = "text default '0.00KZ'")
	private String total_retributions = "0.00KZ";
	@Column(nullable = false, columnDefinition = "text default '0.00KZ'")
	private String total_deductions = "0.00KZ";
	@Column(nullable = false, columnDefinition = "text default '0.00KZ'")
	private String gross_salary = "0.00KZ";
	@Column(nullable = false, columnDefinition = "text default '0.00KZ'")
	private String total_salary_kwanza = "0.00KZ";
	@Column(nullable = false, columnDefinition = "text default '0.00KZ'")
	private String total_salary_dollar = "0.00KZ";
	@Column(nullable = false, columnDefinition = "text default '0.00KZ'")
	private String total_salary_euro = "0.00KZ";
	@Column(nullable = false, columnDefinition = "int(11) default 0")
	private int hours_worked = 0;
	@Column(nullable = false, columnDefinition = "int(11) default 0")
	private int days_worked = 0;
	@Column
	private String extra_hours;
	@Column
	private String status;
	@Column
	private String month_of_admission;
	@Column
	private int year_of_admission;
	@Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date creation_date;
	@Column(nullable = false, columnDefinition = "text default '0.00KZ'")
	private String subsidy_food = "0.00KZ";
	@Column(nullable = false, columnDefinition = "text default '0.00KZ'")
	private String subsidy_transport = "0.00KZ";
	@Column(nullable = false, columnDefinition = "text default '0.00KZ'")
	private String subsidy_christmas = "0.00KZ";
	@Column(nullable = false, columnDefinition = "text default '0.00KZ'")
	private String subsidy_vacation = "0.00KZ";
	@Column(nullable = false, columnDefinition = "text default '0.00KZ'")
	private String subsidy_executive = "0.00KZ";
	@Column(nullable = false, columnDefinition = "text default '0.00KZ'")
	private String subsidy_rent = "0.00KZ";
	@Column(nullable = false, columnDefinition = "text default '0.00KZ'")
	private String subsidy_family = "0.00KZ";
	@Column(nullable = false, columnDefinition = "text default '0.00KZ'")
	private String subsidy_other = "0.00KZ";
	@Column(nullable = false, columnDefinition = "text default '0.00KZ'")
	private String deduction_other = "0.00KZ";
	@Column(nullable = false, columnDefinition = "text default '[]'")
	private String added_retributions = "[]";
	@Column(nullable = false, columnDefinition = "text default '[]'")
	private String added_deductions = "[]";
	@Column
	private String absence_fee;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "YYYY-MM-dd")
	private Date start_date = new Date();
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "YYYY-MM-dd")
	private Date end_date = new Date();
	@Column(nullable = false, columnDefinition = "bigint(20) default 0")
	private long empresa_id = 0;
	
	public Vencimentos() {
		
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

	public int getFuncionario_id() {
		return funcionario_id;
	}

	public void setFuncionario_id(int funcionario_id) {
		this.funcionario_id = funcionario_id;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getProcessamento_id() {
		return processamento_id;
	}

	public void setProcessamento_id(int processamento_id) {
		this.processamento_id = processamento_id;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getInss() {
		return inss;
	}

	public void setInss(String inss) {
		this.inss = inss;
	}

	public String getInss_company() {
		return inss_company;
	}

	public void setInss_company(String inss_company) {
		this.inss_company = inss_company;
	}

	public String getIrt_fee() {
		return irt_fee;
	}

	public void setIrt_fee(String irt_fee) {
		this.irt_fee = irt_fee;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAlt_phone() {
		return alt_phone;
	}

	public void setAlt_phone(String alt_phone) {
		this.alt_phone = alt_phone;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getType_of_contract() {
		return type_of_contract;
	}

	public void setType_of_contract(String type_of_contract) {
		this.type_of_contract = type_of_contract;
	}

	public String getId_document() {
		return id_document;
	}

	public void setId_document(String id_document) {
		this.id_document = id_document;
	}

	public String getAdmission_date() {
		return admission_date;
	}

	public void setAdmission_date(String admission_date) {
		this.admission_date = admission_date;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public int getDependants() {
		return dependants;
	}

	public void setDependants(int dependants) {
		this.dependants = dependants;
	}

	public int getHours_per_day() {
		return hours_per_day;
	}

	public void setHours_per_day(int hours_per_day) {
		this.hours_per_day = hours_per_day;
	}

	public int getDays_per_week() {
		return days_per_week;
	}

	public void setDays_per_week(int days_per_week) {
		this.days_per_week = days_per_week;
	}

	public String getCost_per_hour() {
		return cost_per_hour;
	}

	public void setCost_per_hour(String cost_per_hour) {
		this.cost_per_hour = cost_per_hour;
	}

	public String getBase_salary() {
		return base_salary;
	}

	public void setBase_salary(String base_salary) {
		this.base_salary = base_salary;
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

	public String getTotal_retributions() {
		return total_retributions;
	}

	public void setTotal_retributions(String total_retributions) {
		this.total_retributions = total_retributions;
	}

	public String getTotal_deductions() {
		return total_deductions;
	}

	public void setTotal_deductions(String total_deductions) {
		this.total_deductions = total_deductions;
	}

	public String getGross_salary() {
		return gross_salary;
	}

	public void setGross_salary(String gross_salary) {
		this.gross_salary = gross_salary;
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

	public int getHours_worked() {
		return hours_worked;
	}

	public void setHours_worked(int hours_worked) {
		this.hours_worked = hours_worked;
	}

	public int getDays_worked() {
		return days_worked;
	}

	public void setDays_worked(int days_worked) {
		this.days_worked = days_worked;
	}

	public String getExtra_hours() {
		return extra_hours;
	}

	public void setExtra_hours(String extra_hours) {
		this.extra_hours = extra_hours;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMonth_of_admission() {
		return month_of_admission;
	}

	public void setMonth_of_admission(String month_of_admission) {
		this.month_of_admission = month_of_admission;
	}

	public int getYear_of_admission() {
		return year_of_admission;
	}

	public void setYear_of_admission(int year_of_admission) {
		this.year_of_admission = year_of_admission;
	}

	public Date getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}

	public String getSubsidy_food() {
		return subsidy_food;
	}

	public void setSubsidy_food(String subsidy_food) {
		this.subsidy_food = subsidy_food;
	}

	public String getSubsidy_transport() {
		return subsidy_transport;
	}

	public void setSubsidy_transport(String subsidy_transport) {
		this.subsidy_transport = subsidy_transport;
	}

	public String getSubsidy_christmas() {
		return subsidy_christmas;
	}

	public void setSubsidy_christmas(String subsidy_christmas) {
		this.subsidy_christmas = subsidy_christmas;
	}

	public String getSubsidy_vacation() {
		return subsidy_vacation;
	}

	public void setSubsidy_vacation(String subsidy_vacation) {
		this.subsidy_vacation = subsidy_vacation;
	}

	public String getSubsidy_executive() {
		return subsidy_executive;
	}

	public void setSubsidy_executive(String subsidy_executive) {
		this.subsidy_executive = subsidy_executive;
	}

	public String getSubsidy_rent() {
		return subsidy_rent;
	}

	public void setSubsidy_rent(String subsidy_rent) {
		this.subsidy_rent = subsidy_rent;
	}

	public String getSubsidy_family() {
		return subsidy_family;
	}

	public void setSubsidy_family(String subsidy_family) {
		this.subsidy_family = subsidy_family;
	}

	public String getSubsidy_other() {
		return subsidy_other;
	}

	public void setSubsidy_other(String subsidy_other) {
		this.subsidy_other = subsidy_other;
	}

	public String getDeduction_other() {
		return deduction_other;
	}

	public void setDeduction_other(String deduction_other) {
		this.deduction_other = deduction_other;
	}

	public String getAdded_retributions() {
		return added_retributions;
	}

	public void setAdded_retributions(String added_retributions) {
		this.added_retributions = added_retributions;
	}

	public String getAdded_deductions() {
		return added_deductions;
	}

	public void setAdded_deductions(String added_deductions) {
		this.added_deductions = added_deductions;
	}

	public String getAbsence_fee() {
		return absence_fee;
	}

	public void setAbsence_fee(String absence_fee) {
		this.absence_fee = absence_fee;
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

	public long getEmpresa_id() {
		return empresa_id;
	}

	public void setEmpresa_id(long empresa_id) {
		this.empresa_id = empresa_id;
	}


	
}
