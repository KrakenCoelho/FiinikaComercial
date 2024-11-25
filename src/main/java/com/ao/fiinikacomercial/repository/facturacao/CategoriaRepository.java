package com.ao.fiinikacomercial.repository.facturacao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ao.fiinikacomercial.model.facturacao.Categoria;

import java.util.List;

public interface CategoriaRepository extends CrudRepository<Categoria, Long> { 
	@Query(value="select *  from categoria,empresa where id_empresa = id_empresafk and id_empresafk = ? order by nome_categoria asc", nativeQuery = true)
	Iterable <Categoria> findById_empresa (Long id_empresafk );
	
	@Query(value="select *  from categoria,empresa where id_empresa = id_empresafk and id_empresafk = ? and id_categoria = ?", nativeQuery = true)
	Iterable <Categoria> findById_empresa (Long id_empresafk,long id_categoria );
	
	@Query(value="select *  from categoria where id_empresafk = ? order by id_categoria desc", nativeQuery = true)
	Iterable <Categoria> findAllCategoria (long id_empresa);
	
	
	
}
