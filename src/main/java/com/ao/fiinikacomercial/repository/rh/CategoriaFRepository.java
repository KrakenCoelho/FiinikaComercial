package com.ao.fiinikacomercial.repository.rh;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ao.fiinikacomercial.model.rh.Categorias;

public interface CategoriaFRepository extends CrudRepository<Categorias, Integer> {
	
	@Query(value = "SELECT * FROM categoriaf WHERE empresa_id = ? OR empresa_id = 0 ORDER BY id DESC", nativeQuery = true)
	Iterable <Categorias> findAllCategorias(long id);
	
	@Query(value = "SELECT * FROM categoriaf WHERE id = ?", nativeQuery = true)
	Iterable <Categorias> findById(int id);
	
	@Query(value = "SELECT * FROM categoriaf WHERE id = ?", nativeQuery = true)
	List <Categorias> findById2(int id);
	
	@Query(value = "SELECT name FROM categoriaf WHERE id = ?", nativeQuery = true)
	String findNameById(int id);
	
	@Query(value = "SELECT base_salary FROM categoriaf WHERE id = ?", nativeQuery = true)
	String findBSById(int id);
	
	@Query(value = "SELECT COUNT(id) FROM categoriaf", nativeQuery = true)
	int countById(int id);

}

