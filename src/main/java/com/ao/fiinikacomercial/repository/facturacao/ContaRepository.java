package com.ao.fiinikacomercial.repository.facturacao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ao.fiinikacomercial.model.facturacao.Conta;

public interface ContaRepository extends CrudRepository<Conta, Long> { 
	@Query(value="select *  from conta,empresa where id_empresa = id_empresafk and id_empresafk = ?", nativeQuery = true)
	Iterable <Conta> findById_empresa (Long id_empresafk );
	
	@Query(value="select *  from conta where id_conta = ? and id_empresafk = ?", nativeQuery = true)
	Iterable <Conta> findById (long id_conta,Long id_empresafk );
	
	@Query(value="select iban from conta where iban = ? and id_empresafk = ?", nativeQuery = true)
	String VerifyIban(String iban,long id_iban); 
	
	@Query(value="select *  from conta,empresa where id_empresa = id_empresafk and id_empresafk = ? and padrao = 1", nativeQuery = true)
	Iterable <Conta> findContaPadrao (Long id_empresafk );
	
	
	@Transactional(readOnly = false)
	@Modifying
	@Query(value="update conta set padrao=:padrao where id_conta =:id_conta", nativeQuery = true)
	void updateContaPadrao(int padrao,long id_conta); 
	
}




