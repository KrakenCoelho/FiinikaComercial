package com.ao.fiinikacomercial.repository.rh;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ao.fiinikacomercial.model.rh.Deducoes;

public interface DeducaoRepository extends CrudRepository<Deducoes, Integer> {
	
	@Query(value = "SELECT * FROM deducoes WHERE empresa_id = ? ORDER BY id DESC", nativeQuery = true)
	Iterable <Deducoes> findAllDeducoes(long id);
	
	@Query(value = "SELECT * FROM deducoes WHERE vencimento_id = ? ORDER BY id DESC", nativeQuery = true)
	Iterable <Deducoes> findAllDeducoesByVencimentoId(int id);
	
	@Query(value = "SELECT * FROM deducoes WHERE id = ?", nativeQuery = true)
	Iterable <Deducoes> findDeducaoById(int id);
	
	@Query(value = "SELECT name FROM deducoes WHERE id = ?", nativeQuery = true)
	String findNameById(int id);
	
	@Query(value = "SELECT COUNT(id) FROM deducoes", nativeQuery = true)
	int countDeducoes();

}
