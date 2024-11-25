package com.ao.fiinikacomercial.repository.facturacao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.ao.fiinikacomercial.model.facturacao.Subscricao;

public interface SubscricaoRepository extends CrudRepository<Subscricao, Long> { 
	
	@Query(value="select id_pacotefk ,id_empresafk from subscricao where referencia = ?", nativeQuery = true)
	List <List> findPacoteSubscricao (String referencia);
	
	@Query(value="select id_pacotefk,data_expiracao_pagamento,data_pagamento,tipo from subscricao where estado = ? and id_empresafk = ? order by id_subscricao desc limit 1", nativeQuery = true)
	List <List>  findSubscricaDexa (String estado,long id_empresafk);
	
	@Query(value="select * from subscricao where estado = ? and id_empresafk = ? order by id_subscricao desc limit 1", nativeQuery = true)
	Optional <Subscricao>  findSubscricaDexaOpt (String estado,long id_empresafk);
	
	@Query(value="select * from subscricao where estado = ? and id_empresafk = ? and id_pacotefk = ? order by id_subscricao desc limit 1", nativeQuery = true)
	Optional <Subscricao>  findSubscricaao (String estado,long id_empresafk,long id_pacote);
	
	@Transactional(readOnly = false)
	@Modifying
	@Query(value="update subscricao set estado =:estado,data_pagamento =:data_pagamento,hora_pagamento =:hora_pagamento,data_expiracao_pagamento =:data_expiracao_pagamento,tipo =:tipo where referencia =:referencia and estado='Pendente' ", nativeQuery = true)
	void updateEstadoCompraCliente(String estado,String data_pagamento,String hora_pagamento,String data_expiracao_pagamento,int tipo,String referencia);
	
	@Query(value="select estado from subscricao where referencia = ?", nativeQuery = true)
	String findEstadoSubscricao (String referencia);
	
	@Transactional(readOnly = false)
	@Modifying
	@Query(value="delete from subscricao where estado='Pendente' and data_exp_referencia < CURRENT_DATE ", nativeQuery = true)
	void DeleteRefereciaExpCron();
	
	@Query(value="select * from subscricao  where estado = ? order by id_subscricao desc limit 1", nativeQuery = true)
	Iterable <Subscricao>  findSubscricaDexaCron (String estado);
	
	
	@Query(value="SELECT * FROM subscricao WHERE id_empresafk= ? and tipo = 1 and estado = 'Paga' order by id_subscricao desc limit 1", nativeQuery = true)
	Iterable <Subscricao>  findSubscricaDexa(long id_empresafk);
	
	@Query(value="SELECT * FROM subscricao WHERE id_empresafk= ? and tipo = 1 and estado = 'Paga' order by id_subscricao desc", nativeQuery = true)
	Iterable <Subscricao>  findSubscricaByEmpresa(long id_empresafk);
	
	Optional <Subscricao>  findByIdEmpresafk(long id_empresafk);
	
	@Transactional(readOnly = false)
	@Modifying
	@Query(value="update subscricao set referencia =:referencia,token=:token,data_exp_referencia=:data_exp_referencia where id_subscricao =:id_subscricao ", nativeQuery = true)
	void updateReferencia(String referencia,String token,String data_exp_referencia,long id_subscricao);
	
	 Optional <Subscricao> findByIdEmpresafkAndReferenciaAndToken(long idEmpresa,String referencia,String token);
	

	

}
