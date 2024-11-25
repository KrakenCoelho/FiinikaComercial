package com.ao.fiinikacomercial.repository.facturacao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ao.fiinikacomercial.model.facturacao.Fornecedor;

public interface FornecedorRepository extends CrudRepository<Fornecedor, Long> { 
	
	
	@Query(value="select *  from fornecedor where id_empresafk = ? and id_fornecedor = ?", nativeQuery = true)
	Optional <Fornecedor> findByIdF (Long id_empresafk,long id_fornecedor);
	
	@Query(value="select *  from fornecedor,empresa where id_empresa = id_empresafk and id_empresafk = ? order by id_fornecedor desc", nativeQuery = true)
	Iterable <Fornecedor> findById_empresa (Long id_empresafk );
	
	
	@Query(value="select email from fornecedor where email = ?", nativeQuery = true)
	String VerifyEmail(String email); 
	
	@Query(value="select email from fornecedor where id_empresafk= ? and email = ?", nativeQuery = true)
	String VerifyEmail(long id_empresa,String email); 
	
	@Query(value="select telemovel from fornecedor where id_empresafk= ? and telemovel = ?", nativeQuery = true)
	String VerifyTelemovel(long id_empresa,String telemovel); 
	
	@Query(value="select nif from fornecedor where id_empresafk= ? and nif = ?", nativeQuery = true)
	String VerifyNif(long id_empresafk,String nif); 
	
	@Query(value="select * from fornecedor where nome_fornecedor LIKE %?%", nativeQuery = true)
	Iterable <Fornecedor> findPesquisaDinamica (String nome_fornecedor);
	
	@Query(value="select count(id_fornecedor) from fornecedor where id_empresafk = ? and YEAR(data_cadastro) = ?", nativeQuery = true)
	int count(long id_empresa,String ano);
	
	
	
}
