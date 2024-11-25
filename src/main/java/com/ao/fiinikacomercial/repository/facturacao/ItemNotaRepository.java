package com.ao.fiinikacomercial.repository.facturacao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ao.fiinikacomercial.model.facturacao.ItemNotas;

public interface ItemNotaRepository extends CrudRepository<ItemNotas, Long> {
		
		@Query(value="SELECT * FROM item_notas,factura  WHERE id_factura=id_notafk and id_empresafk=? and id_notafk = ? ", nativeQuery = true)
		Iterable <ItemNotas> findItemFactura (Long id_empresa,Long id_factura );
		
		@Query(value="SELECT count(id_notafk) FROM item_notas,factura  WHERE id_factura=id_notafk and id_empresafk=? and id_notafk = ? ", nativeQuery = true)
		Integer findqtdItem (Long id_empresa,Long id_factura );
		
		
		@Query(value="SELECT * FROM item_notas  WHERE id_notafk = ? ", nativeQuery = true)
		Iterable <ItemNotas> findItemFacturaNota (Long id_facturaNota );

}
