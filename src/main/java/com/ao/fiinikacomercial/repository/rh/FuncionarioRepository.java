package com.ao.fiinikacomercial.repository.rh;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ao.fiinikacomercial.model.rh.Funcionarios;

public interface FuncionarioRepository extends CrudRepository <Funcionarios, Integer>{
	
	@Query(value = "SELECT * FROM funcionarios WHERE (user_id =:email) AND password =:password", nativeQuery = true)
	Iterable <Funcionarios> login(String email, String password);
	
	@Query(value = "SELECT * FROM funcionarios WHERE (user_id =:email OR phone =:email) AND password =:password AND user_type =:user_type", nativeQuery = true)
	Iterable <Funcionarios> verify(Object email,Object password, String user_type);
	
	@Query(value = "SELECT * FROM funcionarios WHERE empresa_id = ? ORDER BY id DESC", nativeQuery = true)
	Iterable <Funcionarios> findAllFuncionarios(long id);
	
	@Query(value = "SELECT * FROM funcionarios WHERE status = ? AND empresa_id = ? ORDER BY id DESC", nativeQuery = true)
	Iterable <Funcionarios> findAllFuncionarios(String status, long id);
	
	@Query(value = "SELECT * FROM funcionarios WHERE status = ? AND id != ? ORDER BY id DESC", nativeQuery = true)
	Iterable <Funcionarios> findAllFuncionariosExceptMe(String permit, int id);
	
	@Query(value = "SELECT * FROM funcionarios WHERE id = ?", nativeQuery = true)
	Iterable <Funcionarios> findById(int id);
	
	@Query(value = "SELECT name FROM funcionarios WHERE id = ?", nativeQuery = true)
	String findNameById(int id);
	
	@Query(value = "SELECT email FROM funcionarios WHERE id = ?", nativeQuery = true)
	String findEmailById(int id);
	
	@Query(value = "SELECT photo FROM funcionarios WHERE id = ?", nativeQuery = true)
	String getPhotoById(int id);
	
	@Query(value = "SELECT COUNT(id) FROM funcionarios WHERE status = ?", nativeQuery = true)
	int countFuncionariosByStatus(String permit);
	
	@Query(value = "SELECT COUNT(id) FROM funcionarios WHERE gender = ?", nativeQuery = true)
	int countFuncionariosByGender(String gender);
	
	@Query(value = "SELECT COUNT(id) FROM funcionarios WHERE month_of_admission = ?", nativeQuery = true)
	int countFuncionariosByMonth(String month);
	
	@Query(value = "SELECT COUNT(id) FROM funcionarios", nativeQuery = true)
	int countAllFuncionarios();
	
	@Query(value = "SELECT COUNT(id) FROM funcionarios WHERE department = ?", nativeQuery = true)
	int countByDep(int department);
	
	@Query(value = "SELECT COUNT(id) FROM funcionarios WHERE employee_status = ?", nativeQuery = true)
	int countEmployeesOnVacation(String employee_status);
	
	@Query(value = "SELECT * FROM funcionarios WHERE department = ? AND status = ? ORDER BY name ASC", nativeQuery = true)
	List <Funcionarios> findFuncionariosByDeparmentId(int id_dep, String permit);
	
	
	//Filter
	@Query(value = "SELECT * FROM funcionarios WHERE name LIKE :search% AND department = :department AND category = :category AND status = :status ORDER BY id DESC", nativeQuery = true)
	List <Funcionarios> filterFuncionariosAll(@Param("search") String search, @Param("department") int department, @Param("category") int category, @Param("status") String status);
	
	@Query(value = "SELECT * FROM funcionarios WHERE name LIKE :search% AND status = :status ORDER BY id DESC", nativeQuery = true)
	List <Funcionarios> filterColaboradorBySearch(@Param("search")String search, @Param("status") String status);
	
	@Query(value = "SELECT * FROM funcionarios WHERE department = ? AND status = ? ORDER BY id DESC", nativeQuery = true)
	List <Funcionarios> filterColaboradorByDepartment(int department, String status);
	
	@Query(value = "SELECT * FROM funcionarios WHERE category = ? AND status = ? ORDER BY id DESC", nativeQuery = true)
	List <Funcionarios> filterColaboradorByCategory(int category, String status);
	
	@Query(value = "SELECT * FROM funcionarios WHERE name LIKE :search% AND category = :category AND status = :status ORDER BY id DESC", nativeQuery = true)
	List <Funcionarios> filterColaboradorBySearchCategory(@Param("search") String search, @Param("category") int category, @Param("status") String status);
	
	@Query(value = "SELECT * FROM funcionarios WHERE name LIKE :search% AND department = :department AND status = :status ORDER BY id DESC", nativeQuery = true)
	List <Funcionarios> filterColaboradorBySearchDepartment(@Param("search") String search, @Param("department") int department, @Param("status") String status);
	
	@Query(value = "SELECT * FROM funcionarios WHERE category = ? AND department = ? AND status = ?  ORDER BY id DESC", nativeQuery = true)
	List <Funcionarios> filterColaboradorByCategoryDepartment(int category, int department, String status);

	@Query(value = "SELECT * FROM funcionarios WHERE status = ? ORDER BY id DESC", nativeQuery = true)
	List <Funcionarios> filterDefault(String status);
	
	
	
	
	@Transactional()
	@Modifying
	@Query(value = " UPDATE funcionarios SET  status= ? WHERE id = ?", nativeQuery = true)
	int updateStatus(String permit, int id);
	
	@Transactional()
	@Modifying
	@Query(value = " UPDATE funcionarios SET  employee_status= ? WHERE id = ?", nativeQuery = true)
	int updateEmployeeStatus(String status, int id);
	
	@Transactional()
	@Modifying
	@Query(value = " UPDATE funcionarios SET  days_worked= ?, hours_worked= ? WHERE id = ?", nativeQuery = true)
	int updateDaysHoursWorkedById(long days_worked, long hours_worked, int id);
	
	@Transactional()
	@Modifying
	@Query(value = " UPDATE funcionarios SET  extra_hours= ?, hours_worked= ? WHERE id = ?", nativeQuery = true)
	int updateExtra_hoursById(String extra, int hours_worked, int id);
	
	@Transactional()
	@Modifying
	@Query(value = " UPDATE funcionarios SET  absence_fee= ? WHERE id = ?", nativeQuery = true)
	int updateAbsenceFeeById(String fee, int id);
	
	

}
