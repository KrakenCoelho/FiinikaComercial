package com.ao.fiinikacomercial.repository.rh;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ao.fiinikacomercial.model.rh.Processamentos;

public interface ProcessamentoRepository extends CrudRepository <Processamentos, Integer>{
	
	@Query(value = "SELECT * FROM processamentos ORDER BY id DESC", nativeQuery = true)
	Iterable <Processamentos> findAllProcessamentos();
	
	@Query(value = "SELECT * FROM processamentos where empresa_id = ? ORDER BY id DESC", nativeQuery = true)
	Iterable <Processamentos> findAllProcessamentos(long id_empresa);
	
	@Query(value = "SELECT * FROM processamentos WHERE status = ? AND employee_id = ? ORDER BY id DESC", nativeQuery = true)
	Iterable <Processamentos> findAllProcessamentosXYByEmployeeId(String status, int id);
	
	@Query(value = "SELECT * FROM processamentos WHERE status = ? ORDER BY id DESC", nativeQuery = true)
	Iterable <Processamentos> findAllProcessamentosXY(String status);
	
	@Query(value = "SELECT * FROM processamentos WHERE id = ?", nativeQuery = true)
	Iterable <Processamentos> findById(int id);
	
	@Query(value = "SELECT name FROM processamentos WHERE id = ?", nativeQuery = true)
	Iterable <Processamentos> findNameById(int id);
	
	@Query(value = "SELECT COUNT(id) FROM processamentos", nativeQuery = true)
	int countById(int id);
	
	@Query(value = "SELECT COUNT(id) FROM processamentos WHERE start_date >= ? AND end_date <= ?", nativeQuery = true)
	int countProcessamentosToday(String date, String date2);
	
	@Query(value = "SELECT COUNT(id) FROM processamentos WHERE year = ?", nativeQuery = true)
	int countProcessamentosThisYear(String year);
	
	/*@Query(value = "SELECT COUNT(id) FROM processamentos WHERE start_date BETWEEN CAST( GETDATE() AS Date )", nativeQuery = true)
	int countTotalAbsentMonth();*/
	
	@Query(value = "SELECT COUNT(id) FROM processamentos WHERE status = ?", nativeQuery = true)
	int countProcessamentosXY(String status);

	
	
	
	
	//Filter
	@Query(value = "SELECT * FROM processamentos where month = ? AND year = ? ORDER BY id DESC", nativeQuery = true)
	List <Processamentos> filterProcessamentosAll(String month, int year);
	
	@Query(value = "SELECT * FROM processamentos WHERE month = ? ORDER BY id DESC", nativeQuery = true)
	List <Processamentos> filterProcessamentoByMonth(String month);
	
	@Query(value = "SELECT * FROM processamentos WHERE year = ? ORDER BY id DESC", nativeQuery = true)
	List <Processamentos> filterProcessamentoByYear(int year);
	
	@Query(value = "SELECT * FROM processamentos ORDER BY id DESC", nativeQuery = true)
	List <Processamentos> filterDefault();	
	
	
				//JUST IN CASE SEARCH IS ADDED
	/*	@Query(value = "SELECT * FROM processamentos where employee_name LIKE :search% AND department_id = :department AND month = :month AND year = :year AND status = :status ORDER BY id DESC", nativeQuery = true)
	List <Processamentos> filterProcessamentosAll(@Param("search") String search, @Param("department") int department, @Param("month") String month, @Param("year") int year, @Param("status") String status);
	
	@Query(value = "SELECT * FROM processamentos where employee_name LIKE :search% AND status = :status ORDER BY id DESC", nativeQuery = true)
	List <Processamentos> filterProcessamentoBySearch(@Param("search")String search, @Param("status") String status);
	
	@Query(value = "SELECT * FROM processamentos where department_id = ? AND status = ? ORDER BY id DESC", nativeQuery = true)
	List <Processamentos> filterProcessamentoByDepartment(int department, String status);
	
	@Query(value = "SELECT * FROM processamentos WHERE month = ? AND status = ? ORDER BY id DESC", nativeQuery = true)
	List <Processamentos> filterProcessamentoByMonth(String month, String status);
	
	@Query(value = "SELECT * FROM processamentos WHERE year = ? AND status = ? ORDER BY id DESC", nativeQuery = true)
	List <Processamentos> filterProcessamentoByYear(int year, String status);
	
	@Query(value = "SELECT * FROM processamentos WHERE employee_name LIKE :search% AND month = :month AND status = :status ORDER BY id DESC", nativeQuery = true)
	List <Processamentos> filterProcessamentoBySearchMonth(@Param("search") String search, @Param("month") String month, @Param("status") String status);
	
	@Query(value = "SELECT * FROM processamentos WHERE employee_name LIKE :search% AND department_id = :department AND status = :status ORDER BY id DESC", nativeQuery = true)
	List <Processamentos> filterProcessamentoBySearchDepartment(@Param("search") String search, @Param("department") int department, @Param("status") String status);
	
	@Query(value = "SELECT * FROM processamentos WHERE employee_name LIKE :search% AND year = :year AND status = :status ORDER BY id DESC", nativeQuery = true)
	List <Processamentos> filterProcessamentoBySearchYear(@Param("search") String search, @Param("year") int year, @Param("status") String status);
	
	@Query(value = "SELECT * FROM processamentos WHERE month = ? AND department_id = ? AND status = ? ORDER BY id DESC", nativeQuery = true)
	List <Processamentos> filterProcessamentoByMonthDepartment(String month, int department, String status);
	
	@Query(value = "SELECT * FROM processamentos WHERE month = ? AND year = ? AND status = ? ORDER BY id DESC", nativeQuery = true)
	List <Processamentos> filterProcessamentoByMonthYear(String month, int year, String status);
	
	@Query(value = "SELECT * FROM processamentos WHERE department = ? AND year = ? AND status = ? ORDER BY id DESC", nativeQuery = true)
	List <Processamentos> filterProcessamentoByDepartmentYear(int department, int year, String status);
	
	@Query(value = "SELECT * FROM processamentos WHERE employee_name LIKE :search% AND department_id = :department AND year = :year AND status = :status ORDER BY id DESC", nativeQuery = true)
	List <Processamentos> filterProcessamentoBySearchDepartmentYear(@Param("search") String search, @Param("department") int department, @Param("year") int year, @Param("status") String status);

	@Query(value = "SELECT * FROM processamentos WHERE employee_name LIKE :search% AND month = :month AND year = :year AND status = :status ORDER BY id DESC", nativeQuery = true)
	List <Processamentos> filterProcessamentoBySearchMonthYear(@Param("search") String search, @Param("month") String month, @Param("year") int year, @Param("status") String status);
	
	@Query(value = "SELECT * FROM processamentos WHERE month = ? AND department_id = ? AND year = ? AND status = ? ORDER BY id DESC", nativeQuery = true)
	List <Processamentos> filterProcessamentoByMonthDepartmentYear(String search, int department, int year, String status);
	
	@Query(value = "SELECT * FROM processamentos WHERE status = ? ORDER BY id DESC", nativeQuery = true)
	List <Processamentos> filterDefault(String status);*/
	
	
	
	@Transactional()
	@Modifying
	@Query(value = " UPDATE processamentos SET status= ? WHERE id = ?", nativeQuery = true)
	int updateProcessamento(String status, int id);
	
	@Transactional()
	@Modifying
	@Query(value = " UPDATE processamentos SET total_net_salary= ?, total_social_security= ?, total_social_security_company= ?, total_irt= ?, total_subsidies= ?, total_discounts= ?, total_gross_salary= ?, total_salary_kwanza= ?, total_salary_dollar= ?, total_salary_euro= ? WHERE id = ?", nativeQuery = true)
	int updateProcessamentoById(String total_net_salary, String inss, String inss_company, String irt, String total_subsydies, String total_deductions, String total_salary, String kwanza, String dollar, String euro, int id);
	
	
	
}
