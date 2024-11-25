package com.ao.fiinikacomercial.repository.facturacao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.ao.fiinikacomercial.model.facturacao.Produto;

public interface ProdutoRepository extends CrudRepository<Produto, Long> { 
	
	@Query(value="select id_produto  from produto where id_empresa_fkp = ?", nativeQuery = true)
	List findIdProdEmp(Long id_empresa);
	
	@Query(value="select id_produto,referencia_categoria,foto_produto,nome_produto,nome_categoria,codigo,mencao,preco_produto,descricao_p from produto,categoria,isencao where id_categoria = id_categoriafk and id_isencaofk = id_isencao and id_empresa_fkp = ? order by id_produto desc", nativeQuery = true)
	Iterable <List> findAllProduto(long id_empresa);
	
	@Query(value="select id_produto,referencia_categoria,foto_produto,nome_produto,nome_categoria,codigo,mencao,preco_produto,descricao_p from produto,categoria,isencao where id_categoria = id_categoriafk and id_isencaofk = id_isencao and id_produto = ?", nativeQuery = true)
	Iterable <List> findProdutoById(long id_produto);
	
	@Query(value="select id_produto,referencia_categoria,foto_produto,nome_produto,nome_categoria,codigo,mencao,preco_produto,descricao_p,referencia from produto,categoria,isencao where id_categoria = id_categoriafk and id_isencaofk = id_isencao and id_produto = ? and id_empresa_fkp = ?", nativeQuery = true)
	Iterable <List> findProdutoById(long id_produto,long id_empresa);
	
	@Query(value="select id_produto,referencia_categoria,foto_produto,nome_produto,nome_categoria,codigo,mencao,preco_produto,descricao from produto,categoria,isencao where id_categoria = id_categoriafk and id_isencaofk = id_isencao and id_categoria = ?", nativeQuery = true)
	Iterable <List> findProdutoByIdCategoria(long id_categoria);
	
	@Query(value="select *  from produto,categoria where id_categoria = id_categoriafk and id_categoriafk = ?", nativeQuery = true)
	Iterable <Produto> findProductoforCategoria(Long id_categoriafk);
	
	@Query(value="select *  from produto,categoria where id_categoria = id_categoriafk and id_empresafk = ? and id_categoriafk = ?", nativeQuery = true)
	Iterable <Produto> findProductoforCategoria(long id_empresafk,Long id_categoriafk);
	
	@Query(value="select id_produto, foto_produto, id_categoriafk, nome_produto, preco_produto, id_isencaofk, taxa_prod,referencia_categoria,tipo_produto from produto,categoria where id_categoria = id_categoriafk and id_categoriafk = ? order by nome_produto asc", nativeQuery = true)
	List <List> findProductoforCategoria2(Long id_categoriafk);
	
	@Query(value="select *  from produto,categoria where id_categoria = id_categoriafk and id_empresafk = ? order by nome_produto asc", nativeQuery = true)
	Iterable <Produto> findProductoEmpresa(Long id_empresa);
	
	@Query(value="select *  from produto,categoria where id_categoria = id_categoriafk and id_empresafk = ? and id_produto = ?", nativeQuery = true)
	Iterable <Produto> findProductoEmpresa(Long id_empresa,long id_produto);
	
	@Query(value="select *  from produto where  id_empresa_fkp = ? and id_produto = ?", nativeQuery = true)
	Optional <Produto> findProductoEmpresaId(long id_empresa,long id_produto); 
	
	@Query(value="select *  from produto,categoria where id_categoria = id_categoriafk and id_empresafk = ? and id_produto = ? and id_categoriafk = ?", nativeQuery = true)
	Optional <Produto> findById(Long id_empresa,long id_produto,long id_categoria);
	
	@Query(value="select distinct id_produto,nome_produto  from categoria,empresa,produto where id_empresa = id_empresafk and id_categoria = id_categoriafk and id_empresafk = ?", nativeQuery = true)
	List <List> findProdudByCatEmp (Long id_empresafk );
	
	@Query(value="select distinct nome_produto,codigo,mencao  from produto,isencao where id_isencao = id_isencaofk  and id_produto = ?", nativeQuery = true)
	List <List> findProdIsencao (Long id_empresafk );
	
	@Query(value="select id_produto,referencia_categoria,foto_produto,nome_produto,nome_categoria,codigo,mencao,preco_produto,descricao_p from produto,categoria,isencao where id_categoria = id_categoriafk and id_isencaofk = id_isencao and id_categoria = ?", nativeQuery = true)
	List <List> findProdutoCategoriaSearch(long id_categeria);
	
	@Query(value="select id_produto,referencia_categoria,foto_produto,nome_produto,nome_categoria,codigo,mencao,preco_produto,descricao_p from produto,categoria,isencao where id_empresa_fkp = ? and id_categoria = id_categoriafk and id_isencaofk = id_isencao and nome_produto LIKE %?%", nativeQuery = true)
	List <List> findPesquisaDinamica(long id_empresa,String id_categeria);
	
	@Query(value="select count(id_produto)  from produto where id_categoriafk = ? ", nativeQuery = true)
	int countProdCategoria (Long id_categoriafk);
	
	@Query(value="select count(id_produto)  from produto where id_categoriafk = ? and id_empresa_fkp = ?", nativeQuery = true)
	int countProdCategoria (Long id_categoriafk,long id_empresa);
	
	
	@Query(value="select *  from produto where id_empresa_fkp = ? and tipo_produto = ?", nativeQuery = true)
	Iterable <Produto> findProducByTipo(Long id_empresa,String tipo);
	
	@Query(value="select count(id_produto) as num from produto where qtd_stock > ? and id_empresa_fkp = ?", nativeQuery = true)
	int countStock(long qtd_stock,long id_empresa);
	
	@Query(value="select count(id_produto) as num from produto where qtd_stock < ? and qtd_stock != 0 and id_empresa_fkp = ? ", nativeQuery = true)
	int countPoucoStock(long qtd_stock,long id_empresa);
	
	@Query(value="select count(id_produto) as num from produto where qtd_stock = ? and id_empresa_fkp = ?", nativeQuery = true)
	int countSemStock(long qtd_stock,long id_empresa);
	
	Optional <Produto> findByReferenciaAndIdEmpresaFkp(String referencia,long id_empresa);
	
	boolean existsByReferenciaAndIdEmpresaFkp(String referencia,long id_empresa);
	
	
	 @Transactional
	 @Modifying
	 @Query(value = "UPDATE produto SET qtd_stock=(qtd_stock-?) WHERE id_produto=?",nativeQuery = true)
	 int actualizqtd_stock( int qtd ,int id);
	
	
	
	
}