package com.ao.fiinikacomercial.repository.facturacao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ao.fiinikacomercial.model.facturacao.Item;

public interface ItemRepository extends CrudRepository<Item, Long> {
	
	@Query(value="SELECT * FROM item,factura  WHERE id_factura=id_facturafk and id_empresafk=? and id_facturafk = ? ", nativeQuery = true)
	Iterable <Item> findItemFactura (Long id_empresa,Long id_factura );
	
	@Query(value="SELECT * FROM item,factura  WHERE id_factura=id_facturafk and id_facturafk = ? ", nativeQuery = true)
	Iterable <Item> findItem (Long id_factura );
	
	@Query(value="SELECT count(id_facturafk) FROM item,factura  WHERE id_factura=id_facturafk and id_empresafk=? and id_facturafk = ? ", nativeQuery = true)
	Integer findqtdItem (Long id_empresa,Long id_factura );
	
	@Query(value="SELECT count(id_item) FROM item  WHERE id_categoriafk = ? and id_empresafork = ? ", nativeQuery = true)
	int countIdCateg (long id_categoriafk,long id_empresa);
	
	@Query(value="SELECT count(id_item) FROM item  WHERE id_produtofk = ? and id_empresafork = ?", nativeQuery = true)
	int countIdProd (long id_produtofk,long id_empresa);
	
	//@Query(value="SELECT count(id_item) FROM item,produto WHERE id_produtofk = id_produto and id_produtofk = ? and id_empresa_fkp = ? ", nativeQuery = true)
   // int countIdProd (long id_produtofk,long id_empresa);
	  

}
