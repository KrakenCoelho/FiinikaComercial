package com.ao.fiinikacomercial.repository.facturacao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ao.fiinikacomercial.model.facturacao.Usuario;

@Repository
public interface userRepository extends JpaRepository<Usuario,Long>{
	
	  Optional<Usuario> findByUsername(String username);
	  Optional<Usuario> findByEmail(String email);
	  Optional<Usuario> findByTelemovel(String telemovel);
	  Optional<Usuario> findByTelemovelAndCodigo(String telemovel,String codigo);
	  
	  @Query(value="select role_name from usuario u,role_model r,"
	  		+ "tb_users_roles tbu where u.id_usuario = tbu.id_usuario and r.role_id=tbu.role_id and u.id_usuario = ?", nativeQuery = true)
	  String  findType(long id);
	  
	  @Query(value="select count(u.id_usuario) from usuario u,role_model r,"
		  		+ "tb_users_roles tbu where u.id_usuario = tbu.id_usuario and r.role_id=tbu.role_id and role_name = ? and MONTH(data_criacao) = ?", nativeQuery = true)
		  Integer  countUsersMes(String role,String mes);
	  
	  boolean existsByEmail(String email);
	  boolean existsByTelemovel(String telemovel);
	  
	  @Query(value="select count(tbu.id_usuario) from usuario u,role_model r,tb_users_roles tbu where u.id_usuario = tbu.id_usuario and r.role_id=tbu.role_id and r.role_name = ?", nativeQuery = true)
	  int countByTipo(String rolename);
	  
	  @Query(value = "SELECT COUNT(*) "
              + "FROM usuario u "
              + "JOIN tb_users_roles tbu ON u.id_usuario = tbu.id_usuario "
              + "JOIN role_model r ON r.role_id = tbu.role_id "
              + "WHERE u.id_usuario = ?1 AND r.role_name = ?2", 
        nativeQuery = true)
      int countByIdAndTipo(long id_user, String rolename);
	  
		@Query(value="select *  from usuario where id_empresa_user = ?", nativeQuery = true)
		Iterable <Usuario> findIdUserEmpresa(Long id_empresa);
		
		@Query(value="select id_usuario  from usuario where id_empresa_user = ? and role = ?", nativeQuery = true)
		long findIdSuper(long id_empresa,String role);
		
		@Query(value="select email from usuario where email = ?", nativeQuery = true)
		String VerifyEmail(String email); 
		
		@Query(value="select telemovel from usuario where telemovel = ?", nativeQuery = true)
		String VerifyTelemovel(String telemovel);
		
		@Query(value="select email from usuario where email = ?  and id_empresa_user = ?", nativeQuery = true)
		String VerifyEmail(String email,long id_empresa); 
		
		@Query(value="select telemovel from usuario where telemovel = ? and id_empresa_user = ?", nativeQuery = true)
		String VerifyTelemovel(String telemovel,long id_empresa);
		
		@Query(value="select telemovel from usuario where telemovel = ?", nativeQuery = true)
		String VerifyTelemovelCodigo(String telemovel);
		
		@Query(value="select * from usuario where id_empresa_user = ?  and tipo = ?", nativeQuery = true)
		Iterable <Usuario> findPesquisaTipo (long id_empresa,String tipo);
		
		@Query(value="select * from usuario where id_empresa_user = ? and username LIKE %?%", nativeQuery = true)
		Iterable <Usuario> findPesquisaDinamica (long id_empresa,String nome);
		
		@Transactional(readOnly = false)
		@Modifying
		@Query(value="update usuario set codigo =:codigo where telemovel =:telemovel ", nativeQuery = true)
		void updateCodigoVerificacao(String codigo,String telemovel);
	  
	  
	

}
