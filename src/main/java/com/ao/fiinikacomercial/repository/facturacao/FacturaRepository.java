package com.ao.fiinikacomercial.repository.facturacao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import com.ao.fiinikacomercial.model.*;
import com.ao.fiinikacomercial.model.facturacao.Factura;

public interface FacturaRepository  extends CrudRepository<Factura, Long> { 
	
	@Query(value="SELECT *  FROM factura where id_empresafk = ? and id_factura = ?", nativeQuery = true)
	Iterable <Factura> findFacturaById (Long id_empresafk,Long id_factura);
	
	@Query(value="SELECT *  FROM factura where id_empresafk = ? and id_factura = ?", nativeQuery = true)
	Optional <Factura> findFacturaAndEmpresa(Long id_empresafk,Long id_factura);
	
	@Query(value="SELECT *  FROM factura where id_empresafk = ? and id_factura = ? and tipo = ?", nativeQuery = true)
	Optional <Factura> findFacturaAndEmpresa(Long id_empresafk,Long id_factura,String tipo);
	
	@Query(value="SELECT *  FROM factura where id_empresafk = ?", nativeQuery = true)
	Iterable <Factura> findFacturaById (Long id_empresafk);
	
	@Query(value="SELECT *  FROM factura where id_empresafk = ? and tipo = ?", nativeQuery = true)
	Iterable <Factura> findFacturaByIdTipo (Long id_empresafk,String tipo);
	
	@Query(value="SELECT *  FROM factura where id_empresafk = ? and tipo != ?", nativeQuery = true)
	Iterable <Factura> findFacturaByIdTipoN (Long id_empresafk,String tipo);
	
	@Query(value="SELECT id_factura,codigo_factura,nome_cliente,total_final,tipo,YEAR(data_emissao),date_format(data_emissao,'%d/%m/%Y'),vendedor,c.foto_logotipo FROM factura f,cliente c,empresa e WHERE e.id_empresa=c.id_empresafk and c.id_empresafk = ? and e.id_empresa=f.id_empresafk and id_cliente=id_clientefk order by id_factura desc limit 0,6 ", nativeQuery = true)
	List <List> FacturasLastsDocumentos(long id_empresa);
	
	@Query(value="SELECT id_factura,codigo_factura,nome_cliente,total_final,tipo,YEAR(data_emissao),date_format(data_emissao,'%d/%m/%Y'),vendedor,c.foto_logotipo FROM factura f,cliente c,empresa e WHERE e.id_empresa=c.id_empresafk and e.id_empresa=f.id_empresafk and id_cliente=id_clientefk and id_vendedorfk = ? order by id_factura desc limit 0,6 ", nativeQuery = true)
	List <List> FacturasLastsDocumentosV(long id_vendedorfk);
	
	// Para pesquisa
	@Query(value="SELECT id_factura,codigo_factura,nome_cliente,total_final,tipo,YEAR(data_emissao),date_format(data_emissao,'%d/%m/%Y'),vendedor,c.foto_logotipo,code_factura, f.id_vendedorfk FROM factura f,cliente c,empresa e WHERE f.id_empresafk = ? and e.id_empresa=c.id_empresafk and e.id_empresa=f.id_empresafk and id_cliente=id_clientefk and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and tipo = ? order by id_factura desc", nativeQuery = true)
	List <List> FacturasLastsDocumentosSearch(long id_empresa,String ano,String mes,String tipo);
	
	//@Query(value="select increment,codigo_factura  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and tipo = 'PP' order by id_factura desc limit 1", nativeQuery = true)
	//List <List>  findLastIncrementProforma (Long id_empresafk);
	
	@Query(value="select increment,codigo_factura  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and tipo = ? order by id_factura desc limit 1", nativeQuery = true)
	List <List>  findLastIncrementFactura (Long id_empresafk, String tipo);
	
	//@Query(value="select increment,codigo_factura  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and tipo = 'FT' order by id_factura desc limit 1", nativeQuery = true)
	//List <List>  findLastIncrementFacturaRecibo (Long id_empresafk);
	
	@Query(value="select id_factura  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? order by id_factura desc limit 1", nativeQuery = true)
	Long  findInsertFactura (Long id_empresafk);
	// Para os filtros
	@Query(value="select *  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and YEAR(data_emissao) = ? order by id_factura desc", nativeQuery = true)
	Iterable <Factura> findFacturasEmpresa (Long id_empresafk,String ano );
	
	// para o saft
	//@Query(value="select *  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and tipo != 'PP' ", nativeQuery = true)
	//Iterable <Factura> findIdFacturaEmpresaF_FR_NC (Long id_empresafk,String ano, String mes );
	
	@Query(value="select *  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and data_emissao >= ? and data_emissao <= ? and tipo != 'PP' ", nativeQuery = true)
	Iterable <Factura> findIdFacturaEmpresaF_FR_NC (Long id_empresafk,String ano, String mes );
	
	//@Query(value="select *  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and tipo = 'PP' ", nativeQuery = true)
	//Iterable <Factura> findIdFacturaEmpresaWORK (Long id_empresafk,String ano, String mes );
	
	@Query(value="select *  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and data_emissao >= ? and data_emissao <= ? and tipo = 'PP' ", nativeQuery = true)
	Iterable <Factura> findIdFacturaEmpresaWORK (Long id_empresafk,String ano, String mes );
	
	@Query(value="select *  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ?", nativeQuery = true)
	Iterable <Factura> findIdFacturaEmpresa2 (Long id_empresafk);
	
	@Query(value="SELECT id_factura,codigo_factura,nome_cliente,total_final,tipo FROM factura f,cliente c,empresa e WHERE e.id_empresa=c.id_empresafk and e.id_empresa=f.id_empresafk and f.id_empresafk = ? and id_cliente=id_clientefk  and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? order by id_factura asc ", nativeQuery = true)
	List <List> FacturasMesAno (Long id_empresafk, String ano, String mes );
	
	//@Query(value="SELECT id_factura,codigo_factura,nome_cliente,total_final FROM factura f,cliente c,empresa e WHERE e.id_empresa=c.id_empresafk and e.id_empresa=f.id_empresafk and f.id_empresafk = ? and id_cliente=id_clientefk and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and tipo = 'FT' order by id_factura asc ", nativeQuery = true)
	//List <List> FacturasMesAnoF (Long id_empresafk, String ano, String mes );

	
	@Query(value="SELECT Year(data_emissao) as ano, Month(data_emissao) as mes FROM factura  WHERE id_factura = ? ", nativeQuery = true)
	List <List> AnoMes (Long id_factura);
	
	@Query(value="SELECT id_factura,codigo_factura,date_format(data_emissao,'%d-%m-%Y'), date_format(data_expiracao,'%d-%m-%Y'),CAST(desconto AS SIGNED),imposto,id_clientefk,total,nome_cliente,increment,estado,total_final,taxa,tipo,YEAR(data_emissao) as ano,c.nif,c.telemovel,c.email,c.endereco,vendedor,obs,banco,numero_conta,iban,id_conta FROM factura f,cliente c,empresa e,conta WHERE e.id_empresa=c.id_empresafk and e.id_empresa=f.id_empresafk and id_contafk = id_conta and f.id_empresafk = ? and id_factura = ? and id_cliente=id_clientefk", nativeQuery = true)
	List <List> findFacturaId (Long id_empresafk,Long id_factura);
	
	@Query(value="SELECT id_factura,codigo_factura,date_format(f.data_emissao,'%d-%m-%Y'), date_format(data_expiracao,'%d-%m-%Y'),desconto,imposto,f.id_clientefk,total,nc.nome_cliente,increment,estado,total_final,taxa,tipo,YEAR(f.data_emissao) as ano,c.nif,c.telemovel,c.email,c.endereco,vendedor,obs,banco,numero_conta,iban,id_conta,motivo FROM factura f,cliente c,empresa e,conta,nota_credito nc WHERE e.id_empresa=c.id_empresafk and e.id_empresa=f.id_empresafk and id_contafk = id_conta and f.id_empresafk = ? and id_factura = ? and id_cliente=f.id_clientefk and id_facturafk = id_factura", nativeQuery = true)
	List <List> findFacturaIdNC (Long id_empresafk,Long id_factura);
	
	@Query(value="SELECT codigo_factura,total_final,id_factura,estado FROM factura where codigo_factura LIKE %?% and estado != '100%' ", nativeQuery = true)
	List <List> findCodFactura (String cod_factura);
	
	@Query(value="select count(id_factura)  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and YEAR(data_emissao) = ? and estado = '100%'", nativeQuery = true)
	int countEntradas (Long id_empresafk,String ano);
	
	@Query(value="select count(id_factura) from factura where id_empresafk = ? and  MONTH(data_emissao) = ?", nativeQuery = true)
	int CountVendasMesFR(long id_empresa,String mes);
	
	@Query(value="select count(id_factura) from factura where id_empresafk = ? and  MONTH(data_emissao) = ? and tipo = 'FR' and estado != 'A'", nativeQuery = true)
	int ProveCountVendasMesFR(long id_empresa,String mes);
	
	//@Query(value="select count(id_factura)  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and tipo != 'PP'", nativeQuery = true)
	//int countEntradasMesF_FR_NCr (Long id_empresafk,String ano,String mes);
	
	@Query(value="select count(id_factura)  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and data_emissao >= ? and data_emissao <= ? and tipo != 'PP'", nativeQuery = true)
	int countEntradasMesF_FR_NC (Long id_empresafk,String ano,String mes);
	
	//@Query(value="select count(id_factura)  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and tipo = 'PP'", nativeQuery = true)
	//int countEntradasMesWORK (Long id_empresafk,String ano,String mes);
	
	@Query(value="select count(id_factura)  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and data_emissao >= ? and data_emissao <= ? and tipo = 'PP'", nativeQuery = true)
	int countEntradasMesWORK (Long id_empresafk,String ano,String mes);
	
	@Query(value="select sum(total_final)  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and YEAR(data_emissao) = ? and estado = '100%'", nativeQuery = true)
	String TotalCredito (Long id_empresafk,String ano);	
	
	//@Query(value="select sum(total)  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and tipo != 'NC' and tipo != 'PP' and estado != 'A'", nativeQuery = true)
	//String TotalCreditoMesF_FR (Long id_empresafk,String ano,String mes);	
	
	@Query(value="select sum(total)  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and data_emissao >= ? and data_emissao <= ? and tipo != 'NC' and tipo != 'PP' and estado != 'A'", nativeQuery = true)
	String TotalCreditoMesF_FR (Long id_empresafk,String data_de,String data_ate);
	
	//@Query(value="select sum(total)  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and tipo = 'NC' and estado != 'A'", nativeQuery = true)
	//String TotalDebitoMesNC (Long id_empresafk,String ano,String mes);
	
	@Query(value="select sum(total)  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and data_emissao >= ? and data_emissao <= ? and tipo = 'NC' and estado != 'A'", nativeQuery = true)
	String TotalDebitoMesNC (Long id_empresafk,String data_de,String data_ate);
	
	//@Query(value="select sum(total)  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and tipo = 'PP' and estado != 'A'", nativeQuery = true)
	//String TotalCreditoMesWORK (Long id_empresafk,String ano,String mes);
	
	@Query(value="select sum(total)  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and data_emissao >= ? and data_emissao <= ? and tipo = 'PP' and estado != 'A'", nativeQuery = true)
	String TotalCreditoMesWORK (Long id_empresafk,String data_de,String data_ate);
	
	//@Query(value="select sum(total)  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and tipo = 'NC' and estado != 'A'", nativeQuery = true)
	//String TotalDebitoMesWORK_NC (Long id_empresafk,String ano,String mes);	
	
	@Query(value="select sum(total)  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and data_emissao >= ? and data_emissao <= ? and tipo = 'NC' and estado != 'A'", nativeQuery = true)
	String TotalDebitoMesWORK_NC (Long id_empresafk,String data_de,String data_ate);	

	//@Query(value="SELECT count(id_factura) FROM factura f,empresa e WHERE  e.id_empresa=f.id_empresafk and f.id_empresafk = ?  and YEAR(data_emissao) = ? and MONTH(data_emissao) = ?", nativeQuery = true)
	//int countFactMes (Long id_empresafk, String ano, String mes );
	
	@Query(value="SELECT count(id_factura) FROM factura f,empresa e WHERE  e.id_empresa=f.id_empresafk and f.id_empresafk = ?  and data_emissao >= ? and data_emissao <= ?", nativeQuery = true)
	int countFactMes (Long id_empresafk, String data_de, String data_ate );
	
	@Query(value="SELECT count(id_factura) FROM factura f,empresa e WHERE  e.id_empresa=f.id_empresafk and f.id_empresafk = ?  and YEAR(data_emissao) = ? and tipo = ?", nativeQuery = true)
	int countFactAnoTipo (long id_empresafk, String ano,String tipo );
	
	// para o control do hash
	//@Query(value="select hash_msg  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and tipo = 'PP' order by id_factura desc limit 1", nativeQuery = true)
	//String  findLastHashPP (Long id_empresafk);
	
	// para o control do hash
	//@Query(value="select hash_msg  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and tipo = 'FT' order by id_factura desc limit 1", nativeQuery = true)
	//String  findLastHashFT (Long id_empresafk);
	
	@Query(value="select hash_msg  from factura,empresa where id_empresa = id_empresafk and id_empresafk = ? and tipo = ? order by id_factura desc limit 1", nativeQuery = true)
	String  findLastHash(Long id_empresafk, String tipo);
	
	@Query(value="Select Min(data_emissao) FROM factura", nativeQuery = true)
	String MenorDataFactura ();
	
	@Query(value="Select Max(data_emissao) FROM factura", nativeQuery = true)
	String MaiorDataFactura ();
	
	@Query(value="Select Min(data_emissao) FROM factura where id_empresafk = ?", nativeQuery = true)
	String MenorDataFactura (long id_empresa);
	
	@Query(value="Select Max(data_emissao) FROM factura where id_empresafk = ?", nativeQuery = true)
	String MaiorDataFactura (long id_empresa);
	
	//====================================
	@Query(value="select  id_factura,codigo_factura,nome_cliente,total_final,tipo,YEAR(data_emissao),date_format(data_emissao,'%d/%m/%Y'),vendedor  from factura,cliente where id_cliente = id_clientefk and factura.id_empresafk = ? and id_clientefk = ? and YEAR(data_emissao) = ? and MONTH(data_emissao) = ?", nativeQuery = true)
	List <List> findFacturaByIdCliente(long id_empresa,Long id_clientefk,String ano,String mes);
	
	@Query(value="select  id_factura,codigo_factura,nome_cliente,total_final,tipo,YEAR(data_emissao),date_format(data_emissao,'%d/%m/%Y'),vendedor  from factura,cliente where id_cliente = id_clientefk and factura.id_empresafk = ? and id_clientefk = ? order by id_factura desc", nativeQuery = true)
	List <List> findFacturaByIdCliente(long id_empresa,Long id_clientefk);
	
	@Query(value="select  id_factura,CAST(codigo_factura AS SIGNED),nome_cliente,total_final,tipo,YEAR(data_emissao),date_format(data_emissao,'%d/%m/%Y'),vendedor,foto_logotipo  from factura,cliente where id_cliente = id_clientefk and factura.id_empresafk = ? and id_contafk = ? and YEAR(data_emissao) = ? and MONTH(data_emissao) = ?", nativeQuery = true)
	List <List> findFacturaByIdConta(long id_empresa,Long id_contafk,String ano,String mes);
	
	@Query(value="select  id_factura,codigo_factura,nome_cliente,total_final,tipo,YEAR(data_emissao),date_format(data_emissao,'%d/%m/%Y'),vendedor,foto_logotipo  from factura,cliente where id_cliente = id_clientefk and factura.id_empresafk = ? and id_contafk = ?", nativeQuery = true)
	List <List> findFacturaByIdConta(long id_empresa,Long id_contafk);
	
	@Query(value="select  id_factura,codigo_factura,nome_cliente,total_final,tipo,YEAR(data_emissao),date_format(data_emissao,'%d/%m/%Y'),vendedor,foto_logotipo,code_factura ,id_vendedorfk  from factura,cliente where factura.id_empresafk = ? and id_cliente = id_clientefk and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? order by id_factura desc", nativeQuery = true)
	List <List> findAllFacturas(long id_empresa,String ano,String mes);
	
	@Query(value="select  id_factura,codigo_factura,nome_cliente,total_final,tipo,YEAR(data_emissao),date_format(data_emissao,'%d/%m/%Y'),vendedor,foto_logotipo,code_factura  from factura,cliente where id_cliente = id_clientefk and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and id_vendedorfk = ? order by id_factura desc", nativeQuery = true)
	List <List> findAllFacturasV(String ano,String mes,long id_vendedorfk);
	
	@Query(value="select increment,codigo_factura,YEAR(data_emissao) from factura where id_empresafk = ? and YEAR(data_emissao) = ? and tipo = ? order by id_factura desc limit 1", nativeQuery = true)
	List <List>  findLastIncrement(long id_empresa,String ano,String tipo);
	
	@Transactional(readOnly = false)
	@Modifying
	@Query(value="update factura set code_factura =:code_factura where id_factura =:id_factura ", nativeQuery = true)
	void updateCode(String code_factura,long id_factura);
	
	@Query(value="select sum(total_final) from factura  where id_empresafk = ? and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and estado != 'A' and tipo = 'FR'", nativeQuery = true)
	String TotalFacturaRecibo (long id_empresa,String ano,String mes);
	
	@Query(value="select sum(total_final) from factura  where YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and tipo = 'FR' and estado != 'A' and id_vendedorfk = ?", nativeQuery = true)
	String TotalFacturaReciboV (String ano,String mes,long id_vendedorfk);
	
	@Query(value="select sum(total_final) from factura  where YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and id_clientefk = ? and tipo = 'FR'", nativeQuery = true)
	String TotalFacturaRecibo (String ano,String mes,long id_clientefk);
	
	@Query(value="select sum(total_final) from factura  where id_empresafk = ? and id_clientefk = ? and tipo = 'FR'", nativeQuery = true)
	String TotalFacturaRecibo (long id_empresafk,long id_clientefk);
	
	@Query(value="select sum(total_final) from factura  where id_empresafk = ? and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and id_contafk = ? and tipo = 'FR'", nativeQuery = true)
	String TotalFacturaReciboBanco (long id_empresa,String ano,String mes,long id_contafk);
	
	@Query(value="select sum(total_final) from factura  where id_empresafk = ? and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and tipo = 'FT' and estado != 'A'", nativeQuery = true)
	String TotalFactura (long id_empresa,String ano,String mes);
	
	@Query(value="select COALESCE(sum(total_final),0) from factura  where id_empresafk = ? and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and tipo = 'FT' and estado != 'A' and estado != '100%'", nativeQuery = true)
	String TotalPagar (long id_empresa,String ano,String mes);
	
	@Query(value="select sum(total_final) from factura  where YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and tipo = 'FT' and estado != 'A' and id_vendedorfk = ?", nativeQuery = true)
	String TotalFacturaV (String ano,String mes,long id_vendedorfk);
	
	@Query(value="select sum(total_final) from factura  where id_empresafk = ? and YEAR(data_emissao) = ? and tipo = 'FT' and estado != 'A'", nativeQuery = true)
	String TotalFactura (long id_empresa,String ano);
	
	@Query(value="select sum(total_final) from factura  where id_empresafk = ? and YEAR(data_emissao) = ? and (tipo = 'FT' or tipo = 'FR') and estado != 'A'", nativeQuery = true)
	String TotalFacturado (long id_empresa,String ano);
	
	@Query(value="select sum(total_final) from factura  where id_empresafk = ? and YEAR(data_emissao) = ? and tipo = 'PP'", nativeQuery = true)
	String TotalProforma (long id_empresa,String ano);
	
	@Query(value="select sum(total_final) from factura  where YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and id_clientefk = ? and tipo = 'FT'", nativeQuery = true)
	String TotalFactura (String ano,String mes,long id_clientefk);
	
	@Query(value="select sum(total_final) from factura  where id_empresafk = ? and id_clientefk = ? and tipo = 'FT'", nativeQuery = true)
	String TotalFactura (long id_empresa,long id_clientefk);
	
	@Query(value="select sum(total_final) from factura  where id_empresafk = ? and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and id_contafk = ? and tipo = 'FT'", nativeQuery = true)
	String TotalFacturaBanco (long id_empresa,String ano,String mes,long id_contafk);
	
	@Query(value="select sum(total_final) from factura,recibo  where id_empresafk = ? and id_factura = id_facturafk and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and estado = 'A'", nativeQuery = true)
	String TotalRembolsoFtAnuladas (long id_empresa,String ano,String mes);
	
	@Query(value="select sum(total_final) from factura  where id_empresafk = ? and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and tipo = 'FR' and estado = 'A'", nativeQuery = true)
	String TotalRembolsoFrAnuladas (long id_empresa,String ano,String mes);
	
	@Query(value="select sum(total_final) from factura,recibo  where id_factura = id_facturafk and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and estado = 'A' and factura.id_vendedorfk = ?", nativeQuery = true)
	String TotalRembolsoFtAnuladasV (String ano,String mes,long id_vendedorfk);
	
	@Query(value="select sum(total_final) from factura  where  YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and estado = 'A' and tipo = 'FR' and id_vendedorfk = ?", nativeQuery = true)
	String TotalRembolsoFrAnuladasV (String ano,String mes,long id_vendedorfk);

	@Query(value="select sum(total_final) from factura,recibo  where id_factura = id_facturafk and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and id_clientefk = ? and estado = 'A'", nativeQuery = true)
	String TotalRembolsoFtAnuladas (String ano,String mes,long id_clientefk);
	
	@Query(value="select sum(total_final) from factura  where  YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and id_clientefk = ? and tipo = 'FR' and estado = 'A'", nativeQuery = true)
	String TotalRembolsoFrAnuladas (String ano,String mes,long id_clientefk);
	
	@Query(value="select sum(total_final) from factura,recibo  where id_factura = id_facturafk and id_empresafk = ? and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and factura.id_contafk = ? and estado = 'A'", nativeQuery = true)
	String TotalRembolsoFtAnuladasBanco (long id_empresa,String ano,String mes,long id_contafk);
	
	@Query(value="select sum(total_final) from factura  where id_empresafk = ? and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and id_contafk = ? and tipo = 'FR' and estado = 'A'", nativeQuery = true)
	String TotalRembolsoFrAnuladasBanco (long id_empresa,String ano,String mes,long id_contafk);
	
	@Query(value="SELECT id_factura,total_final,origem  from factura f,nota_credito n WHERE id_empresafk = ? and tipo = 'NC' and id_factura = id_facturafk and motivo = 'Rectificação' and YEAR(f.data_emissao) = ? and MONTH(f.data_emissao) = ?", nativeQuery = true)
	List <List>  findNotasReembolso(long id_empresa,String ano,String mes);
	
	@Query(value="SELECT id_factura,total_final,origem  from factura f,nota_credito n WHERE id_empresafk = ? and tipo = 'NC' and id_factura = id_facturafk and motivo = 'Rectificação' and YEAR(f.data_emissao) = ? and MONTH(f.data_emissao) = ? and id_vendedorfk = ?", nativeQuery = true)
	List <List>  findNotasReembolsoV(long id_empresa,String ano,String mes,long id_vendedorfk);
	
	@Query(value="SELECT id_factura,total_final,origem  from factura f,nota_credito n WHERE id_empresafk = ? and tipo = 'NC' and id_factura = id_facturafk and motivo = 'Rectificação' and YEAR(f.data_emissao) = ? and MONTH(f.data_emissao) = ? and f.id_clientefk = ?", nativeQuery = true)
	List <List>  findNotasReembolso(long id_empresa,String ano,String mes,long id_clientefk);
	
	@Query(value="SELECT id_factura,total_final,origem  from factura f,nota_credito n WHERE id_empresafk = ? and tipo = 'NC' and id_factura = id_facturafk and motivo = 'Rectificação' and YEAR(f.data_emissao) = ? and MONTH(f.data_emissao) = ? and f.id_contafk = ?", nativeQuery = true)
	List <List>  findNotasReembolsoBanco(long id_fcatura,String ano,String mes,long id_contafk);
	
	//Query(value="SELECT total_final  from factura WHERE id_empresafk = ? and codigo_factura = ?", nativeQuery = true)
	//List <List> findFacturaByOrigem(long id_empresa,String origem);
	
	@Query(value="SELECT total_final  from factura WHERE id_empresafk = ? and codigo_factura = ? order by id_factura desc limit 0,1", nativeQuery = true)
	String findFacturaByOrigemUniq(long id_empresa,String origem);
	
	@Query(value="select *  from factura  where id_empresafk = ? order by id_factura desc", nativeQuery = true)
	Iterable <Factura> findAll (long id_empresa);
	
	@Query(value="select  id_factura,codigo_factura,nome_cliente,total_final,tipo,YEAR(data_emissao),date_format(data_emissao,'%d/%m/%Y'),vendedor,foto_logotipo,code_factura,id_vendedorfk  from factura,cliente where factura.id_empresafk = ? and id_cliente = id_clientefk and YEAR(data_emissao) = ? and tipo = ? order by id_factura desc", nativeQuery = true)
	List <List> findAllFacturasAnoTipo(long id_empresa,String ano,String tipo);
	
	@Query(value="select  id_factura,codigo_factura,nome_cliente,total_final,tipo,YEAR(data_emissao),date_format(data_emissao,'%d/%m/%Y'),vendedor,foto_logotipo,code_factura,id_vendedorfk  from factura,cliente where factura.id_empresafk = ? and id_cliente = id_clientefk and YEAR(data_emissao) = ? order by id_factura desc", nativeQuery = true)
	List <List> findAllFacturasAno(long id_empresa,String ano);
	
	@Query(value="select  id_factura,codigo_factura,nome_cliente,total_final,tipo,YEAR(data_emissao),date_format(data_emissao,'%d/%m/%Y'),vendedor,foto_logotipo,code_factura ,id_vendedorfk from factura,cliente where factura.id_empresafk = ? and id_cliente = id_clientefk and MONTH(data_emissao) = ? and tipo = ? order by id_factura desc", nativeQuery = true)
	List <List> findAllFacturasMesTipo(long id_empresa,String mes,String tipo);
	
	@Query(value="select  id_factura,codigo_factura,nome_cliente,total_final,tipo,YEAR(data_emissao),date_format(data_emissao,'%d/%m/%Y'),vendedor,foto_logotipo,code_factura ,id_vendedorfk  from factura,cliente where factura.id_empresafk = ? and id_cliente = id_clientefk and MONTH(data_emissao) = ? order by id_factura desc", nativeQuery = true)
	List <List> findAllFacturasMes(long id_empresa,String mes);
	
	@Query(value="select  id_factura,codigo_factura,nome_cliente,total_final,tipo,YEAR(data_emissao),date_format(data_emissao,'%d/%m/%Y'),vendedor,foto_logotipo,code_factura,id_vendedorfk  from factura,cliente where factura.id_empresafk = ? and id_cliente = id_clientefk and tipo = ? order by id_factura desc", nativeQuery = true)
	List <List> findAllFacturasTipo(long id_empresa,String tipo);
	
	
	@Query(value="SELECT id_factura,codigo_factura,nome_cliente,total_final,tipo,YEAR(data_emissao),date_format(data_emissao,'%d/%m/%Y'),vendedor,foto_logotipo,code_factura  FROM    factura,cliente where factura.id_empresafk = ? and id_cliente = id_clientefk and tipo = ? and codigo_factura  LIKE %?%  order by id_factura desc", nativeQuery = true)
	List <List> findPesquisaDinamica (long id_empresa,String tipo,String codigo_factura);
	
	@Query(value="SELECT id_factura,codigo_factura,nome_cliente,total_final,tipo,YEAR(data_emissao),date_format(data_emissao,'%d/%m/%Y'),vendedor,foto_logotipo,code_factura  FROM    factura,cliente where factura.id_empresafk = ? and id_cliente = id_clientefk and codigo_factura  LIKE %?%  order by id_factura desc", nativeQuery = true)
	List <List> findPesquisaDinamica (long id_empresa,String codigo_factura);
	
	@Query(value="select *  from factura  where referente = ? and id_empresafk = ? ", nativeQuery = true)
	Iterable <Factura> findReferente (String referente,long id_empresa);
	
	@Query(value="SELECT * FROM factura where code_factura =? ", nativeQuery = true)
	List <List> findCodeFactura (String cod_factura);	
	
	@Query(value="select sum(total_final) from factura  where id_empresafk = ? and YEAR(data_emissao) = ? and tipo = 'FR' and estado != 'A'", nativeQuery = true)
	String TotalFacturadoFR (long id_empresa,String ano);
	
	@Query(value="SELECT AVG(mensal) AS media_mensal FROM (select sum(total_final) as mensal from factura  where id_empresafk = ? and YEAR(data_emissao) = ? and tipo = 'FR' and estado != 'A' GROUP BY MONTH(data_emissao)) as Fmensal", nativeQuery = true)
	String AvgTotalFacturadoFR (long id_empresa,String ano);
	
	@Query(value="select sum(total_final) from factura  where id_empresafk = ? and YEAR(data_emissao) = ? and MONTH(data_emissao) = ? and tipo = 'FR' and estado != 'A'", nativeQuery = true)
	String TotalFacturadoFR (long id_empresa,String ano,String mes);
	
	@Query(value="select * from factura  where id_empresafk = ? and YEAR(data_emissao) = ? and tipo = 'FR' and estado != 'A'", nativeQuery = true)
	Iterable <Factura> findFacturaRecibo (long id_empresa,String ano);
	 
	//===== verifica repetição
	@Query(value="SELECT *  from factura WHERE id_empresafk = ? and codigo_factura = ?", nativeQuery = true)
	Iterable <Factura> findFacturaExists(long id_empresa,String codigo_factura);
	
	
	@Query(value=" SELECT * FROM factura WHERE data_emissao BETWEEN ? AND ? AND id_empresafk = ?", nativeQuery = true)
	Iterable <Factura> findFacturaDeAte(String de,String ate,long id_empresa);
	
	
	
	
	



}
