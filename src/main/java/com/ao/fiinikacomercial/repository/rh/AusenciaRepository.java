package com.ao.fiinikacomercial.repository.rh;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ao.fiinikacomercial.model.rh.Ausencias;

public interface AusenciaRepository extends CrudRepository <Ausencias, Integer>{
	
	@Query(value = "SELECT * FROM ausencias ORDER BY id DESC", nativeQuery = true)
	Iterable <Ausencias> findAllAusencias();
	
	@Query(value = "SELECT * FROM ausencias WHERE empresa_id = ? ORDER BY id DESC", nativeQuery = true)
	Iterable <Ausencias> findAllByEmpresaId(long empresa_id);
	
	@Query(value = "SELECT * FROM ausencias ORDER BY id", nativeQuery = true)
	Iterable <Ausencias> findAusenciaById(int id);
	
	@Query(value = "SELECT * FROM ausencias WHERE status = ? AND employee_id = ? ORDER BY id", nativeQuery = true)
	Iterable <Ausencias> findAllAusenciasXYByEmployeeId(String status, int id);
	
	@Query(value = "SELECT * FROM ausencias WHERE status = ? ORDER BY id DESC", nativeQuery = true)
	Iterable <Ausencias> findAllAusenciasXY(String status);
	
	@Query(value = "SELECT * FROM ausencias WHERE month = ? AND status = ? AND employee_id = ? ORDER BY id DESC", nativeQuery = true)
	Iterable <Ausencias> findAllByMonthAndStatus(String month, String status, int employee_id);
	
	@Query(value = "SELECT * FROM ausencias WHERE month = ? AND status = ? AND employee_id = ? AND id = ? ORDER BY id DESC", nativeQuery = true)
	Iterable <Ausencias> findByMonthAndStatusId(String month, String status, int employee_id, int id);
	
	@Query(value = "SELECT * FROM ausencias WHERE start_date BETWEEN ? AND ? AND end_date BETWEEN ? AND ? ORDER BY id DESC", nativeQuery = true)
	Iterable <Ausencias> findAllByProcessamentoDates(String start_date, String end_date, String start_date2, String end_date2);
	
	@Query(value = "SELECT * FROM ausencias WHERE id = ?", nativeQuery = true)
	Iterable <Ausencias> findById(int id);
	
	@Query(value = "SELECT * FROM ausencias WHERE employee_id = ?", nativeQuery = true)
	Iterable <Ausencias> findByEmployeeId(int id);
	
	@Query(value = "SELECT name FROM ausencias WHERE id = ?", nativeQuery = true)
	Iterable <Ausencias> findNameById(int id);
	
	@Query(value = "SELECT * FROM ausencias WHERE motive = ?", nativeQuery = true)
	Iterable <Ausencias> findByMotive(String motive);
	
	@Query(value = "SELECT * FROM ausencias WHERE start_date <= ? AND end_date >= ? AND motive = ?", nativeQuery = true)
	Iterable <Ausencias> findByDatesMotive(String sDate, String sDate2, String motive);
	
	@Query(value = "SELECT COUNT(id) FROM ausencias", nativeQuery = true)
	int countById(int id);
	
	@Query(value = "SELECT COUNT(id) FROM ausencias WHERE start_date <= ? AND end_date >= ?", nativeQuery = true)
	int countAusenciasToday(String date, String date2);
	
	/*@Query(value = "SELECT COUNT(id) FROM ausencias WHERE year = ?", nativeQuery = true)
	int countAusenciasThisYear(String year);;*/
	
	@Query(value = "SELECT COUNT(id) FROM ausencias WHERE month = ?", nativeQuery = true)
	int countTotalAbsentMonth(String month);
	
	@Query(value = "SELECT COUNT(id) FROM ausencias WHERE month = ? AND motive = ? AND employee_id = ?", nativeQuery = true)
	int countTotalEmployeeVacationsMonthMotive(String month, String motive, int id);
	
	@Query(value = "SELECT COUNT(id) FROM ausencias WHERE month = ? AND status = ? AND employee_id = ?", nativeQuery = true)
	int countTotalAbsentByMonthStatusId(String month, String status, int id);
	
	@Query(value = "SELECT COUNT(id) FROM ausencias WHERE status = ?", nativeQuery = true)
	int countAusenciasXY(String status);
	
	@Query(value = "SELECT COUNT(id) FROM ausencias WHERE motive = ?", nativeQuery = true)
	int countAusenciasMotive(String motive);

	
	
	
	
	//Filter
	@Query(value = "SELECT * FROM ausencias where employee_name LIKE :search% AND department_id = :department AND month = :month AND year = :year AND status = :status ORDER BY id DESC", nativeQuery = true)
	List <Ausencias> filterAusenciasAll(@Param("search") String search, @Param("department") int department, @Param("month") String month, @Param("year") int year, @Param("status") String status);
	
	@Query(value = "SELECT * FROM ausencias where employee_name LIKE :search% AND status = :status ORDER BY id DESC", nativeQuery = true)
	List <Ausencias> filterAusenciaBySearch(@Param("search")String search, @Param("status") String status);
	
	@Query(value = "SELECT * FROM ausencias where department_id = ? AND status = ? ORDER BY id DESC", nativeQuery = true)
	List <Ausencias> filterAusenciaByDepartment(int department, String status);
	
	@Query(value = "SELECT * FROM ausencias WHERE month = ? AND status = ? ORDER BY id DESC", nativeQuery = true)
	List <Ausencias> filterAusenciaByMonth(String month, String status);
	
	@Query(value = "SELECT * FROM ausencias WHERE year = ? AND status = ? ORDER BY id DESC", nativeQuery = true)
	List <Ausencias> filterAusenciaByYear(int year, String status);
	
	@Query(value = "SELECT * FROM ausencias WHERE employee_name LIKE :search% AND month = :month AND status = :status ORDER BY id DESC", nativeQuery = true)
	List <Ausencias> filterAusenciaBySearchMonth(@Param("search") String search, @Param("month") String month, @Param("status") String status);
	
	@Query(value = "SELECT * FROM ausencias WHERE employee_name LIKE :search% AND department_id = :department AND status = :status ORDER BY id DESC", nativeQuery = true)
	List <Ausencias> filterAusenciaBySearchDepartment(@Param("search") String search, @Param("department") int department, @Param("status") String status);
	
	@Query(value = "SELECT * FROM ausencias WHERE employee_name LIKE :search% AND year = :year AND status = :status ORDER BY id DESC", nativeQuery = true)
	List <Ausencias> filterAusenciaBySearchYear(@Param("search") String search, @Param("year") int year, @Param("status") String status);
	
	@Query(value = "SELECT * FROM ausencias WHERE month = ? AND department_id = ? AND status = ? ORDER BY id DESC", nativeQuery = true)
	List <Ausencias> filterAusenciaByMonthDepartment(String month, int department, String status);
	
	@Query(value = "SELECT * FROM ausencias WHERE month = ? AND year = ? AND status = ? ORDER BY id DESC", nativeQuery = true)
	List <Ausencias> filterAusenciaByMonthYear(String month, int year, String status);
	
	@Query(value = "SELECT * FROM ausencias WHERE department = ? AND year = ? AND status = ? ORDER BY id DESC", nativeQuery = true)
	List <Ausencias> filterAusenciaByDepartmentYear(int department, int year, String status);
	
	@Query(value = "SELECT * FROM ausencias WHERE employee_name LIKE :search% AND department_id = :department AND year = :year AND status = :status ORDER BY id DESC", nativeQuery = true)
	List <Ausencias> filterAusenciaBySearchDepartmentYear(@Param("search") String search, @Param("department") int department, @Param("year") int year, @Param("status") String status);

	@Query(value = "SELECT * FROM ausencias WHERE employee_name LIKE :search% AND month = :month AND year = :year AND status = :status ORDER BY id DESC", nativeQuery = true)
	List <Ausencias> filterAusenciaBySearchMonthYear(@Param("search") String search, @Param("month") String month, @Param("year") int year, @Param("status") String status);
	
	@Query(value = "SELECT * FROM ausencias WHERE month = ? AND department_id = ? AND year = ? AND status = ? ORDER BY id DESC", nativeQuery = true)
	List <Ausencias> filterAusenciaByMonthDepartmentYear(String search, int department, int year, String status);
	
	@Query(value = "SELECT * FROM ausencias WHERE status = ? ORDER BY id DESC", nativeQuery = true)
	List <Ausencias> filterDefault(String status);
	
	
}
