package com.ao.fiinikacomercial.repository.rh;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ao.fiinikacomercial.model.rh.Vencimentos;

public interface VencimentoRepository extends CrudRepository <Vencimentos, Integer>{
	
	@Query(value = "SELECT * FROM vencimentos WHERE (user_id =:email) AND password =:password", nativeQuery = true)
	Iterable <Vencimentos> login(String email, String password);
	
	@Query(value = "SELECT * FROM vencimentos WHERE (user_id =:email OR phone =:email) AND password =:password AND user_type =:user_type", nativeQuery = true)
	Iterable <Vencimentos> verify(Object email,Object password, String user_type);
	
	@Query(value = "SELECT * FROM vencimentos WHERE status = ? ORDER BY id DESC", nativeQuery = true)
	Iterable <Vencimentos> findAllVencimentos();
	
	@Query(value = "SELECT * FROM vencimentos WHERE status = ? ORDER BY id DESC", nativeQuery = true)
	Iterable <Vencimentos> findAllVencimentos(String status);
	
	@Query(value = "SELECT * FROM vencimentos WHERE status = ? AND id != ? ORDER BY id DESC", nativeQuery = true)
	Iterable <Vencimentos> findAllVencimentosExceptMe(String permit, int id);
	
	@Query(value = "SELECT * FROM vencimentos WHERE id = ?", nativeQuery = true)
	Iterable <Vencimentos> findById(int id);
	
	@Query(value = "SELECT * FROM vencimentos WHERE processamento_id = ?", nativeQuery = true)
	Iterable <Vencimentos> findByProcessamentoId(int id);
	
	@Query(value = "SELECT * FROM vencimentos WHERE funcionario_id = ? AND status = 'Y'", nativeQuery = true)
	Iterable <Vencimentos> findByFuncionarioId(int id);
	
	@Query(value = "SELECT name FROM vencimentos WHERE id = ?", nativeQuery = true)
	String findNameById(int id);
	
	@Query(value = "SELECT photo FROM vencimentos WHERE id = ?", nativeQuery = true)
	String getPhotoById(int id);
	
	@Query(value = "SELECT COUNT(id) FROM vencimentos WHERE status = ?", nativeQuery = true)
	int countVencimentosByStatus(String permit);
	
	@Query(value = "SELECT COUNT(id) FROM vencimentos WHERE gender = ?", nativeQuery = true)
	int countVencimentosByGender(String gender);
	
	@Query(value = "SELECT COUNT(id) FROM vencimentos WHERE month_of_admission = ?", nativeQuery = true)
	int countVencimentosByMonth(String month);
	
	@Query(value = "SELECT COUNT(id) FROM vencimentos", nativeQuery = true)
	int countAllVencimentos();
	
	@Query(value = "SELECT COUNT(id) FROM vencimentos WHERE processamento_id = ?", nativeQuery = true)
	int countByProcessmatoId(int procecamento_id);
	
	@Query(value = "SELECT COUNT(id) FROM vencimentos WHERE employee_status = ?", nativeQuery = true)
	int countEmployeesOnVacation(String employee_status);
	
	@Query(value = "SELECT * FROM vencimentos WHERE department = ? AND status = ? ORDER BY name ASC", nativeQuery = true)
	List <Vencimentos> findVencimentosByDeparmentId(int id_dep, String permit);
	
	
	//Filter
	@Query(value = "SELECT * FROM vencimentos WHERE name LIKE :search% AND department = :department AND category = :category AND status = :status ORDER BY id DESC", nativeQuery = true)
	List <Vencimentos> filterVencimentosAll(@Param("search") String search, @Param("department") int department, @Param("category") int category, @Param("status") String status);
	
	@Query(value = "SELECT * FROM vencimentos WHERE name LIKE :search% AND status = :status ORDER BY id DESC", nativeQuery = true)
	List <Vencimentos> filterColaboradorBySearch(@Param("search")String search, @Param("status") String status);
	
	@Query(value = "SELECT * FROM vencimentos WHERE department = ? AND status = ? ORDER BY id DESC", nativeQuery = true)
	List <Vencimentos> filterColaboradorByDepartment(int department, String status);
	
	@Query(value = "SELECT * FROM vencimentos WHERE category = ? AND status = ? ORDER BY id DESC", nativeQuery = true)
	List <Vencimentos> filterColaboradorByCategory(int category, String status);
	
	@Query(value = "SELECT * FROM vencimentos WHERE name LIKE :search% AND category = :category AND status = :status ORDER BY id DESC", nativeQuery = true)
	List <Vencimentos> filterColaboradorBySearchCategory(@Param("search") String search, @Param("category") int category, @Param("status") String status);
	
	@Query(value = "SELECT * FROM vencimentos WHERE name LIKE :search% AND department = :department AND status = :status ORDER BY id DESC", nativeQuery = true)
	List <Vencimentos> filterColaboradorBySearchDepartment(@Param("search") String search, @Param("department") int department, @Param("status") String status);
	
	@Query(value = "SELECT * FROM vencimentos WHERE category = ? AND department = ? AND status = ?  ORDER BY id DESC", nativeQuery = true)
	List <Vencimentos> filterColaboradorByCategoryDepartment(int category, int department, String status);

	@Query(value = "SELECT * FROM vencimentos WHERE status = ? ORDER BY id DESC", nativeQuery = true)
	List <Vencimentos> filterDefault(String status);
	
	
	
	@Transactional()
	@Modifying
	@Query(value = " UPDATE vencimentos SET  status= ? WHERE processamento_id = ?", nativeQuery = true)
	int updateRecieveStatus(String status, int processamento_id);
}
