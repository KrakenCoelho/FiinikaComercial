package com.ao.fiinikacomercial.repository.facturacao;

	import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.data.jpa.repository.Query;

import com.ao.fiinikacomercial.model.facturacao.Cliente;


	public interface ClienteRepository extends JpaRepository<Cliente, Long> { 
		
		@Query(value="select * from cliente,empresa where id_empresa = id_empresafk and id_empresafk = ?", nativeQuery = true)
		Iterable <Cliente> findById_empresa (Long id_empresafk );
		
		@Query(value="select * from cliente,empresa where id_empresa = id_empresafk and id_empresafk = ? and id_cliente = ?", nativeQuery = true)
		Optional <Cliente> findById(Long id_empresafk , long id_cliente);
		
		@Query(value="select * from cliente where id_empresafk = ? order by id_cliente desc", nativeQuery = true)
		Iterable <Cliente> findAllClientes (long id_empresa);
		
		@Query(value="SELECT count(id_clientefk) FROM factura WHERE id_clientefk = ?", nativeQuery = true)
		int countIdCliente (long id_clientefk);
		
		@Query(value="select email from cliente where email = ? and id_empresafk = ?", nativeQuery = true)
		String VerifyEmail(String email,long id_empresa); 
		
		//@Query(value="select email from cliente where email = ?", nativeQuery = true)
		//String VerifyEmail(String email); 
		
		@Query(value="select telemovel from cliente where telemovel = ? and id_empresafk = ?", nativeQuery = true)
		String VerifyTelemovel(String telemovel,long id_empresa); 
		
		//@Query(value="select telemovel from cliente where telemovel = ?", nativeQuery = true)
		//String VerifyTelemovel(String telemovel);
		
		@Query(value="select nif from cliente where nif = ? and id_empresafk = ?", nativeQuery = true)
		String VerifyNif(String nif,long id_empresa);
		
		//@Query(value="select nif from cliente where nif = ?", nativeQuery = true)
		//String VerifyNif(String nif);
		
		@Query(value="select * from cliente where id_empresafk = ? and nome_cliente LIKE %?%", nativeQuery = true)
		Iterable <Cliente> findPesquisaDinamica (long id_empresa,String nome_produto);
		
		@Query(value="select *  from cliente where id_empresafk = ? and  YEAR(data_cadastro) = ?", nativeQuery = true)
		Iterable <Cliente> findCliente(long id_empresa,String ano);
		
		@Query(value="select count(id_cliente) from cliente where id_empresafk = ? and YEAR(data_cadastro) = ?", nativeQuery = true)
		int  count(long id_empresa,String ano);
		
		@Query(value="select count(id_cliente) from cliente where id_empresafk = ? and  MONTH(data_cadastro) = ?", nativeQuery = true)
		int CountClientesMes(long id_empresa,String mes);
		
		@Query(value="select count(id_cliente) from cliente where id_empresafk = ? and data_cadastro >= ?", nativeQuery = true)
		int  countNovosClientes(long id_empresa,String data);
		
		  boolean existsByEmail(String email);
		  boolean existsByTelemovel(Integer telemovel);
		  boolean existsByNif(String nif);
		
		
		
		
	}

