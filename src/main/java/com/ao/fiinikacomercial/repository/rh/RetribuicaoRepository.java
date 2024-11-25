package com.ao.fiinikacomercial.repository.rh;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ao.fiinikacomercial.model.rh.Retribuicoes;

public interface RetribuicaoRepository extends CrudRepository<Retribuicoes, Integer> {
	
	@Query(value = "SELECT * FROM retribuicoes WHERE empresa_id = ? OR empresa_id = 0 ORDER BY id DESC", nativeQuery = true)
	Iterable <Retribuicoes> findAllRetribuicoes(long id);
	
	@Query(value = "SELECT * FROM retribuicoes WHERE vencimento_id = ? ORDER BY id DESC", nativeQuery = true)
	Iterable <Retribuicoes> findAllRetribuicoesByVencimentoId(int id);
	
	@Query(value = "SELECT * FROM retribuicoes WHERE id = ?", nativeQuery = true)
	Iterable <Retribuicoes> findRetribuicaoById(int id);
	
	@Query(value = "SELECT * FROM retribuicoes WHERE id = ? AND status = ?", nativeQuery = true)
	Iterable <Retribuicoes> findRetribuicaoByIdStatus(int id, String status);
	
	@Query(value = "SELECT name FROM retribuicoes WHERE id = ?", nativeQuery = true)
	String findNameById(int id);
	
	@Query(value = "SELECT COUNT(id) FROM retribuicoes", nativeQuery = true)
	int countRetribuicoes();

}
