package com.ao.fiinikacomercial.repository.facturacao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ao.fiinikacomercial.model.facturacao.NotaCredito;

public interface NotaCreditoRepository  extends CrudRepository<NotaCredito, Long> { 
	
	@Query(value="select *  from nota_credito where id_facturafk = ?", nativeQuery = true)
	Iterable <NotaCredito> findNotacreditoIdFact (long id_factura );
	
	@Query(value="select *  from nota_credito where id_facturafk = ?", nativeQuery = true)
	Optional <NotaCredito> findNotacreditoId(long id_factura );
	
	@Query(value="select *  from nota_credito where id_factura_origem = ?", nativeQuery = true)
	Optional <NotaCredito> findNotacreditoIdOrigem(long id_facturaOrigem );

}
