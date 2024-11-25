package com.ao.fiinikacomercial.repository.facturacao;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ao.fiinikacomercial.model.facturacao.Empresa;

@Repository
public interface EmpresaRepository extends CrudRepository<Empresa, Long> { 
	@Query(value="select *  from usuario,empresa where id_empresa_user = id_empresa and id_empresa = ?", nativeQuery = true)
	Iterable <Empresa> findIdUser(Long id_usuarioFK );
	
	@Query(value="select *  from empresa where id_empresa = ?", nativeQuery = true)
	Iterable <Empresa> findEmpresa(Long id_empresa );
	
	@Query(value="select foto_logotipo  from empresa where id_empresa = ?", nativeQuery = true)
	String  findImage(Long id_empresa );
	
	@Query(value="select *  from empresa where id_empresa != ?", nativeQuery = true)
	Iterable <Empresa> findDataEmprise(Long id_empresa);
	
	@Query(value="SELECT email,telemovel,telemovel2,nif,website from empresa where id_empresa!=? UNION SELECT email,telemovel,telemovel2,nif,website from cliente where id_cliente!=? UNION SELECT email,telemovel,telemovel2,nif,website from fornecedor where id_fornecedor!=?", nativeQuery=true)
	List <List> findDadosVerify(Long id_empresa,Long id_cliente,Long id_fornecedor);
	
	// Pega nome usuario logado empresa
	@Query(value="select nome  from usuario,empresa where id = id_usuariofk and id_usuariofk = ?", nativeQuery = true)
	String findNomeUser(Long id_usuarioFK );
	
	@Query(value="select nif from empresa where nif = ?", nativeQuery = true)
	String VerifyNif(String nif);
	
	@Query(value="SELECT * from empresa order by id_empresa desc limit 10", nativeQuery=true)
	Iterable <Empresa> findAlll();
	
	@Query(value="SELECT * from empresa order by id_empresa desc limit ?", nativeQuery=true)
	Iterable <Empresa> findAlll(int limit);
	
	@Query(value="select * from empresa where nome_empresa LIKE %?%", nativeQuery = true)
	Iterable <Empresa> findPesquisaDinamica (String nome_empresa);
	
	 @Query(
		      value = "SELECT id_Empresa, nome_empresa,endereco, telemovel, email FROM empresa,pedido WHERE id_empresa=pedido.empresa_idfk AND id_pedido=?",
		      nativeQuery = true
		   )
		   List<List> dadosEntrega(Long idPedido);
	
	
	 @Query(value="SELECT email FROM empresa,pedido WHERE id_empresa = pedido.empresa_idfk AND pedido.id_pedido=?", nativeQuery = true)
		String  email(Long pedidoid );
	 
	 @Query(value="SELECT nome_empresa  FROM empresa WHERE id_empresa = ?", nativeQuery = true)
		String  NomeEmpresa(Long id_empresa );
	 
}










