package com.ao.fiinikacomercial.repository.facturacao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.ao.fiinikacomercial.model.*;
import com.ao.fiinikacomercial.model.facturacao.Recibo;

public interface ReciboRepository  extends CrudRepository<Recibo, Long> { 
	
	@Query(value="select id_recibo,date_format(data_recibo,'%d/%m/%Y') as data_recibo  from recibo order by id_recibo desc limit 0,6", nativeQuery = true)
	List <List> findLastsRecibo ();
	
	@Query(value="select *,date_format(data_recibo,'%d/%m/%Y') as data_recibo  from recibo where id_recibo = ?", nativeQuery = true)
	Iterable <Recibo> findReciboId (Long id_recibo);
	
	@Query(value="select *  from recibo,factura where id_factura = id_facturafk and id_clientefk = ? and YEAR(data_recibo) = ? and MONTH(data_recibo) = ?", nativeQuery = true)
	Iterable <Recibo> findReciboIdCliente (Long id_clientefk,String ano, String mes);
	
	@Query(value="select *  from recibo,factura where id_factura = id_facturafk and id_empresa_fkr = ? and id_clientefk = ? order by id_recibo desc", nativeQuery = true)
	Iterable <Recibo> findReciboIByCliente(long id_empresa,Long id_clientefk);
	
	@Query(value="select id_recibo,codigo_recibo,nome_cliente,vendedor_recibo,date_format(data_recibo,'%d/%m/%Y') as data_recibo,total_recibo,foto_logotipo  from recibo r,factura,cliente where id_factura = id_facturafk and id_clientefk=id_cliente and r.id_contafk = ? and YEAR(data_recibo) = ? and MONTH(data_recibo) = ?", nativeQuery = true)
	List <List> findReciboIdConta (Long id_contafk,String ano, String mes);
	
	@Query(value="select id_recibo,codigo_recibo,nome_cliente,vendedor_recibo,date_format(data_recibo,'%d/%m/%Y') as data_recibo,total_recibo,foto_logotipo  from recibo r,factura,cliente where id_empresa_fkr = ? and id_factura = id_facturafk and id_clientefk=id_cliente and r.id_contafk = ?", nativeQuery = true)
	List <List> findReciboIdConta (long id_empresa,Long id_contafk);
	
	@Query(value="select id_recibo,codigo_recibo,nome_cliente,vendedor_recibo,date_format(data_recibo,'%d/%m/%Y') as data_recibo,valor_pago,foto_logotipo,code_recibo,r.id_vendedorfk  from recibo r,factura,cliente where id_empresa_fkr = ? and id_factura = id_facturafk and id_clientefk=id_cliente and YEAR(data_recibo) = ? and MONTH(data_recibo) = ? order by id_recibo desc", nativeQuery = true)
	List <List> findAllRecibos (long id_empresa,String ano, String mes);
	
	@Query(value="select id_recibo,codigo_recibo,nome_cliente,vendedor_recibo,date_format(data_recibo,'%d/%m/%Y') as data_recibo,valor_pago,foto_logotipo,code_recibo  from recibo r,factura,cliente where id_factura = id_facturafk and id_clientefk=id_cliente and YEAR(data_recibo) = ? and MONTH(data_recibo) = ? and r.id_vendedorfk = ? order by id_recibo desc", nativeQuery = true)
	List <List> findAllRecibosV (String ano, String mes,long id_vendedorfk);
	
	@Query(value="select id_recibo,codigo_recibo,nome_cliente,vendedor_recibo,date_format(data_recibo,'%d/%m/%Y') as data_recibo,valor_pago,foto_logotipo,code_recibo,r.id_vendedorfk  from recibo r,factura,cliente where id_empresa_fkr = ? and id_factura = id_facturafk and id_clientefk=id_cliente and YEAR(data_recibo) = ? order by id_recibo desc", nativeQuery = true)
	List <List> findAllRecibosAno (long id_empresa,String ano);
	
	@Query(value="select id_recibo,codigo_recibo,nome_cliente,vendedor_recibo,date_format(data_recibo,'%d/%m/%Y') as data_recibo,valor_pago,foto_logotipo,code_recibo,r.id_vendedorfk  from recibo r,factura,cliente where id_empresa_fkr = ? and id_factura = id_facturafk and id_clientefk=id_cliente and MONTH(data_recibo) = ? order by id_recibo desc", nativeQuery = true)
	List <List> findAllRecibosMes (long id_empresa,String mes);
	
	@Query(value="select *  from recibo r,factura f,empresa e where r.id_facturafk = f.id_factura and id_empresa = f.id_empresafk and id_empresafk = ? and YEAR(data_recibo) = ?", nativeQuery = true)
	Iterable <Recibo> findIdReciboEmpresa (Long id_empresafk, String ano);
	
	@Query(value="select data_recibo  from recibo r,factura f,empresa e where r.id_facturafk = f.id_factura and id_empresa = f.id_empresafk and id_empresafk = ? and YEAR(data_recibo) = ? UNION select data_emissao from factura  where id_empresafk = ? and YEAR(data_emissao) = ? and tipo = 'FR' and estado != 'A' ORDER BY data_recibo ASC", nativeQuery = true)
	List <List> findIdReciboEmpresa (Long id_empresafk, String ano,Long id_empresafk2, String ano2);
	
	@Query(value="select increment_rec,codigo_recibo,YEAR(data_recibo)  from recibo where id_empresa_fkr = ? and YEAR(data_recibo) = ? order by id_recibo desc limit 1", nativeQuery = true)
	List <List> findLastIncrementRecibo (long id_empresa,String ano);
	
	@Query(value="select id_recibo, estado_recibo, id_contafk, percentagem, data_recibo, num_factura, valor_pago, id_facturafk,data_emissao,id_clientefk,imposto,data_hora_recibo,data_pagamento  from recibo r,factura f,empresa e where r.id_facturafk = f.id_factura and id_empresa = f.id_empresafk and id_empresafk = ? and YEAR(data_recibo) = ?", nativeQuery = true)
	List <List> findIdReciboEmpresaAux (Long id_empresafk, String ano);
	
	@Query(value="select id_recibo, estado_recibo, r.id_contafk, percentagem, data_recibo, num_factura, valor_pago, id_facturafk,data_emissao,id_clientefk,imposto,data_hora_recibo,data_pagamento,taxa_recibo,total_recibo,tipo  from recibo r,factura f,empresa e where r.id_facturafk = f.id_factura and id_empresa = f.id_empresafk and id_empresafk = ? and YEAR(data_recibo) = ? and MONTH(data_recibo) = ?", nativeQuery = true)
	List <List> findIdReciboEmpresaAuxMes (Long id_empresafk, String ano,String mes);
	
	@Query(value="select id_recibo, estado_recibo, r.id_contafk, percentagem, data_recibo, num_factura, valor_pago, id_facturafk,data_emissao,id_clientefk,imposto,data_hora_recibo,data_pagamento,taxa_recibo,total_recibo,tipo,codigo_recibo,codigo_factura  from recibo r,factura f,empresa e where r.id_facturafk = f.id_factura and id_empresa = f.id_empresafk and id_empresafk = ? and  data_emissao  >= ? and data_emissao <= ?", nativeQuery = true)
	List <List> findIdReciboEmpresaAuxDeAte (Long id_empresafk, String ano,String mes);
	
	@Query(value="select id_facturafk  from recibo r,factura f,empresa e where r.id_facturafk = f.id_factura and id_empresa = f.id_empresafk and id_empresafk = ? and YEAR(data_recibo) = ?", nativeQuery = true)
	List <Long> IdFactRecibo (Long id_empresafk, String ano);
	
	@Query(value="select *  from recibo r,factura f,empresa e where r.id_facturafk = f.id_factura and id_empresa = f.id_empresafk and id_facturafk = ?", nativeQuery = true)
	List <String> findIdReciboEmpresaAux (Long id_facturafk);
	
	@Query(value="select *  from recibo r,factura f,empresa e where r.id_facturafk = f.id_factura and id_empresa = f.id_empresafk and id_empresafk = ?", nativeQuery = true)
	Iterable <Recibo> findIdReciboEmpresa2 (Long id_empresafk);
	
	@Query(value="SELECT id_recibo,num_factura,r.percentagem,valor_pago,nome_cliente,codigo_recibo  FROM recibo r,factura f,cliente c,empresa e WHERE r.id_facturafk=f.id_factura and e.id_empresa=c.id_empresafk and e.id_empresa=f.id_empresafk and f.id_empresafk = ? and id_cliente=id_clientefk and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and estado != '0%' ", nativeQuery = true)
	List <List> RecibosFactura (Long id_empresafk, String ano, String mes );
	
	@Query(value="SELECT sum(valor_pago)  FROM recibo r,empresa e,factura f  WHERE id_facturafk=id_factura and id_empresa=id_empresafk and id_empresafk = ? and YEAR(data_recibo) = ? and MONTH(data_recibo) = ? ", nativeQuery = true)
	Float EntradasMesCurrent (Long id_empresafk,String ano, String mes );
	
	@Query(value="SELECT data_recibo AS date,COUNT(id_recibo) AS recibos FROM recibo where YEAR(data_recibo) = ? and MONTH(data_recibo) = ?  GROUP BY data_recibo", nativeQuery = true)
	List <List> GraficoEntradas (String ano,String mes);
	
	@Query(value="SELECT data_recibo AS date,COUNT(id_recibo) AS recibos FROM recibo where YEAR(data_recibo) = ? and MONTH(data_recibo) = ?  GROUP BY MONTH(data_recibo)", nativeQuery = true)
	List <List> GraficoEntradasNew (String ano,String mes);
	
	@Query(value="select count(id_facturafk)  from recibo r,factura f,empresa e where r.id_facturafk = f.id_factura and id_empresa = f.id_empresafk and id_facturafk = ?", nativeQuery = true)
	int countPrestacao (Long id_facturafk);
	
	@Query(value="select count(id_recibo) from recibo r,factura f,empresa e where r.id_facturafk = f.id_factura and id_empresa = f.id_empresafk and id_empresafk = ? and YEAR(data_emissao) = ? and MONTH(data_emissao) = ?", nativeQuery = true)
	int countEntradasMes (Long id_empresafk, String ano,String mes);
	
	@Query(value="select count(id_recibo) from recibo r,factura f,empresa e where r.id_facturafk = f.id_factura and id_empresa = f.id_empresafk and id_empresafk = ? and  data_emissao  >= ? and data_emissao <= ?", nativeQuery = true)
	int countEntradasDeAte (Long id_empresafk, String ano,String mes);
	
	//@Query(value="select sum(total_final)  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and estado = '100%'", nativeQuery = true)
	//String TotalCreditoMes (Long id_empresafk,String ano,String mes);
	
	@Query(value="select sum(total_recibo) from recibo r,factura f,empresa e where r.id_facturafk = f.id_factura and id_empresa = f.id_empresafk and id_empresafk = ? and YEAR(data_emissao) = ? and MONTH(data_emissao) = ?", nativeQuery = true)
	String TotalCreditoMes (Long id_empresafk, String ano,String mes);
	
	@Query(value="select sum(total_recibo) from recibo r,factura f,empresa e where r.id_facturafk = f.id_factura and id_empresa = f.id_empresafk and id_empresafk = ? and data_emissao  >= ? and data_emissao <= ?", nativeQuery = true)
	String TotalCreditoDeAte (Long id_empresafk, String de,String ate);
	
	
	@Query(value="select sum(valor_pago) from recibo  where id_empresa_fkr = ? and YEAR(data_recibo) = ? and MONTH(data_recibo) = ?", nativeQuery = true)
	String TotalRecibo(long id_empresa,String ano,String mes);	
	
	@Query(value="select sum(valor_pago) from recibo  where YEAR(data_recibo) = ? and MONTH(data_recibo) = ? and id_vendedorfk = ?", nativeQuery = true)
	String TotalReciboV (String ano,String mes,long id_vendedorfk);
	
	@Query(value="select sum(valor_pago) from recibo  where id_empresa_fkr = ? and YEAR(data_recibo) = ?", nativeQuery = true)
	String TotalRecibo (long id_empresa,String ano);
	
	@Query(value="SELECT AVG(mensal) AS media_mensal FROM (select sum(valor_pago) as mensal from recibo  where id_empresa_fkr = ? and YEAR(data_recibo) = ? GROUP BY MONTH(data_recibo)) as Fmensal", nativeQuery = true)
	String AvgTotalRecibo (long id_empresa,String ano);
	
	@Query(value="select sum(valor_pago) from recibo,factura  where  id_empresa_fkr = ? and id_factura = id_facturafk and YEAR(data_recibo) = ? and MONTH(data_recibo) = ? and id_clientefk = ?", nativeQuery = true)
	String TotalRecibo (long id_empresa,String ano,String mes,long id_clientefk);
	
	@Query(value="select sum(valor_pago) from recibo,factura  where  id_empresa_fkr = ? and id_factura = id_facturafk and id_clientefk = ?", nativeQuery = true)
	String TotalRecibo (long id_empresa,long id_clientefk);
	
	@Query(value="select sum(valor_pago) from recibo,factura  where id_empresa_fkr = ? and id_factura = id_facturafk and YEAR(data_recibo) = ? and MONTH(data_recibo) = ? and recibo.id_contafk = ?", nativeQuery = true)
	String TotalReciboBanco (long id_empresa,String ano,String mes,long id_contafk);
	
	@Transactional(readOnly = false)
	@Modifying
	@Query(value="update recibo set code_recibo =:code_recibo where id_recibo =:id_recibo ", nativeQuery = true)
	void updateCode(String code_recibo,long id_recibo);
		
	
	@Query(value="select id_recibo,codigo_recibo,nome_cliente,vendedor_recibo,date_format(data_recibo,'%d/%m/%Y') as data_recibo,valor_pago,foto_logotipo,code_recibo,r.id_vendedorfk  from recibo r,factura,cliente where id_empresa_fkr = ? and id_factura = id_facturafk and id_clientefk=id_cliente order by id_recibo desc", nativeQuery = true)
	List <List> findAllRecibos(long id_empresa);
	
	@Query(value="select id_recibo,codigo_recibo,nome_cliente,vendedor_recibo,date_format(data_recibo,'%d/%m/%Y') as data_recibo,valor_pago,foto_logotipo,code_recibo,r.id_vendedorfk  from recibo r,factura,cliente where id_factura = id_facturafk and id_clientefk=id_cliente and codigo_recibo LIKE %?% and id_empresa_fkr = ? order by id_recibo desc", nativeQuery = true)
	List <List> findAllReciboDinamica(String codigo_recibo,long id_empresa);
	
	
	@Query(value="select data_recibo  from recibo where id_empresa_fkr = ? and  YEAR(data_recibo) = ?", nativeQuery = true)
	List <List> findData(long id_empresa,String ano);
	
	@Query(value="select count(id_recibo) from recibo where id_empresa_fkr = ? and  MONTH(data_recibo) = ?", nativeQuery = true)
	int CountVendasMes(long id_empresa,String mes);
	
	@Query(value="select *  from recibo  where  id_empresa_fkr = ? and id_facturafk = ?", nativeQuery = true)
	Iterable <Recibo> findIdRecibo  (Long id_empresafk,long id_factura);
	
	@Query(value="select *  from recibo  where  id_empresa_fkr = ? and id_recibo = ?", nativeQuery = true)
	Iterable <Recibo> findIdReciboEmpresa(Long id_empresafk,long id_recibo);
	
	@Query(value="select *  from recibo  where  id_empresa_fkr = ?", nativeQuery = true)
	Iterable <Recibo> findIdReciboByIdEmpresa  (Long id_empresafk);
	
	@Query(value="select *  from recibo where  code_recibo = ?", nativeQuery = true)
	List <List> findBhyCodeRecibo(String code_recibo);
	
	@Query(value="select id_recibo,codigo_recibo,nome_cliente,vendedor_recibo,date_format(data_recibo,'%d/%m/%Y') as data_recibo,total_recibo,foto_logotipo  from recibo r,factura,cliente where id_factura = id_facturafk and id_clientefk=id_cliente and  id_recibo = ?", nativeQuery = true)
	List <List> findReciboIdCliente (Long id_recibo);
	                    
	

	
	
	
	
	

}
